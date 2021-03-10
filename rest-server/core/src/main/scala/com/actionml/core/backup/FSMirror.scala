/*
 * Copyright ActionML, LLC under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * ActionML licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.actionml.core.backup

import com.actionml.core.validate.JsonSupport
import zio.duration._
import zio.{IO, Queue, Schedule, Task, ZIO}

import java.io._
import java.nio.charset.StandardCharsets

/**
  * Mirror implementation for local FS.
  */
class FSMirror(override val mirrorContainer: String, override val engineId: String) extends Mirror with JsonSupport {

  private var putEventToQueue: String => Task[Unit] = _
  private def runWriteLoop() = {
    for {
      q <- Queue.unbounded[String]
      _ = putEventToQueue = s => q.offer(s).unit
      f = new File(containerName)
      _ = if (!f.exists()) f.mkdirs()
      _ = logger.info(s"Engine-id: ${engineId}; Mirror raw un-validated events to $containerName")
      out <- ZIO.effect(new OutputStreamWriter(new BufferedOutputStream(new FileOutputStream(s"$containerName/$batchName.json", true)), StandardCharsets.UTF_8))
      _ <- ZIO.effect(out.flush()).repeat(Schedule.linear(2.seconds)).fork
      _ <- {
        for {
          l <- q.take
          _ <- ZIO.effect(out.append(l))
        } yield ()
      }.forever
    } yield ()
  }.retry(Schedule.fibonacci(1.milli)).forever
  zio.Runtime.default.unsafeRunAsync(runWriteLoop())(_ => logger.error("FS mirror write error"))

  override def mirrorEvent(event: String): Task[Unit] =
    putEventToQueue(event)
      .onError { c =>
        c.failures.foreach(e => logger.error("Problem mirroring while input", e))
        IO.unit
      }

  override def cleanup(): Task[Unit] = IO.unit
}

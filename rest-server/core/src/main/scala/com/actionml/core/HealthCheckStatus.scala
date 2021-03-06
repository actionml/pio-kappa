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
package com.actionml.core

import com.actionml.core.HealthCheckStatus.HealthCheckStatus
import com.actionml.core.model.Response

object HealthCheckStatus extends Enumeration {
  type HealthCheckStatus = Value

  val green: HealthCheckStatus = Value("green")
  val yellow: HealthCheckStatus = Value("yellow")
  val red: HealthCheckStatus = Value("red")
}

//case class HealthCheckResponse(mongo: HealthCheckStatus, elasticsearch: HealthCheckStatus) extends Response
case class HealthCheckResponse(mongoStatus: String, elasticsearchStatus: String) extends Response
object HealthCheckResponse {
  def apply(mongo: HealthCheckStatus, es: HealthCheckStatus): HealthCheckResponse = HealthCheckResponse(mongo.toString, es.toString)
}

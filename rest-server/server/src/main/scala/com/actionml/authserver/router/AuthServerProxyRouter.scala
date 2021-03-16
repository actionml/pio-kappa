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

package com.actionml.authserver.router

import akka.actor.ActorSystem
import akka.http.scaladsl.server.Route
import akka.stream.ActorMaterializer
import com.actionml.authserver.services.AuthServerProxyService
import com.actionml.core.config.AppConfig
import com.actionml.router.http.routes.BaseRouter

import scala.concurrent.ExecutionContext

class AuthServerProxyRouter(authProxyService: AuthServerProxyService)(
  implicit val actorSystem: ActorSystem,
  implicit protected val executor: ExecutionContext,
  implicit protected val materializer: ActorMaterializer,
  implicit val config: AppConfig
) extends BaseRouter {

  override val route: Route =
    (pathPrefix("auth") & extractRequest) { req =>
      onSuccess(authProxyService.proxyAuthRequest(req)) {
        complete(_)
      }
    }
}

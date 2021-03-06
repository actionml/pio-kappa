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

package com.actionml.core.store.backends

import com.mongodb.MongoClientURI


case class MongoConfig(uri: MongoClientURI, sparkUri: MongoClientURI)

object MongoConfig {

  val mongo = {
    val mongoUri =
      new MongoClientURI(sys.env.getOrElse("MONGO_URI", throw new RuntimeException("Environment variable MONGO_URI must be set")))
    val sparkMongoUri = sys.env.get("SPARK_MONGO_URI").fold(mongoUri)(new MongoClientURI(_))

    MongoConfig(uri = mongoUri, sparkUri = sparkMongoUri)
  }
}
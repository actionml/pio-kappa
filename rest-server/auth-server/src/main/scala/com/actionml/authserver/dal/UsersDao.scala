package com.actionml.authserver.dal

import com.actionml.authserver.model.UserAccount

import scala.concurrent.Future

trait UsersDao {
  def find(id: String): Future[Option[UserAccount]]
  def find(id: String, secretHash: String): Future[Option[UserAccount]]
  def list(offset: Int, limit: Int): Future[Iterable[UserAccount]]
  def update(user: UserAccount): Future[_]
}
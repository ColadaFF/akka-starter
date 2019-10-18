package co.com.ias.lab.scala

import slick.jdbc.H2Profile
import slick.lifted.TableQuery
import slick.jdbc.H2Profile.api._

import scala.concurrent.Future

class UsersDAO(db: H2Profile.backend.Database, users: TableQuery[DatabaseConfig.Users]) {
  def create(id: Int, name: String, lastName: String): Future[(Int, String, String)] = {
    db.run {
      (
        // proyección
        users.map(user => (user.id, user.name, user.lastName))
          // columnas generadas
          returning users.map(_.id)
          // transformación
          into((tuple, id) => (tuple._1, tuple._2, tuple._3))
        ) += (id, name, lastName)
    }
  }

  def findAll: Future[Seq[(Int, String, String)]] = db.run(users.result)
}
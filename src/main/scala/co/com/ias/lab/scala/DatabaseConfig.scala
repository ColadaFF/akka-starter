package co.com.ias.lab.scala

import slick.jdbc.H2Profile
import slick.jdbc.H2Profile.api._
import slick.lifted.{ForeignKeyQuery, Tag}

import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success}


trait DatabaseConfig {
  val db: H2Profile.backend.Database = Database.forConfig("h2mem")
}

object DatabaseConfig {
  class Users(tag: Tag) extends Table[(Int, String, String)](tag, "USERS") {
    def id = column[Int]("USER_ID", O.PrimaryKey)

    def name = column[String]("NAME")

    def lastName = column[String]("LAST_NAME")

    def * = (id, name, lastName)
  }

  val users: TableQuery[Users] = TableQuery[Users]

  class Account(tag: Tag) extends Table[(Int, Int, Int)](tag, "ACCOUNTS") {

    def id = column[Int]("ID", O.PrimaryKey)

    def userId = column[Int]("USER_ID")

    def balance = column[Int]("BALANCE")

    override def * = (id, userId, balance)

    def user: ForeignKeyQuery[Users, (Int, String, String)] =
      foreignKey("USER_PK", userId, users)(_.id)
  }

  val accounts = TableQuery[Account]

  def setupDb(db: H2Profile.backend.Database)(implicit ec:ExecutionContext) = {

    val setupDbAction = DBIO.seq(
      (users.schema ++ accounts.schema).create,
      users += (1, "Luis", "Perez"),
      users += (2, "Luis", "Gomez"),
      users += (3, "Luis", "Jaramillo"),
      accounts += (1, 1, 0),
      accounts += (2, 2, 0),
      accounts += (3, 2, 0)
    )

    val setupFuture: Future[Unit] = db.run(setupDbAction)

    setupFuture.onComplete {
      case Failure(exception) =>
        exception.printStackTrace()
        Console.err.println("Could not setup database")
      case Success(value) =>
        println(s"Setup Database complete: $value")
        db.run(users.result).map(_.foreach {
          case (i, str, str1) => println(s"$i, $str, $str1")
        })

    }
  }

}

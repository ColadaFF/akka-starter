

def multiplyByN(number: Int, multiplier: Int) = {
  number * multiplier
}

multiplyByN(1, 3)
multiplyByN(2, 3)
multiplyByN(3, 3)

def multiplyByN(number: Int)(by: Int) = {
  number * by
}


implicit val multiplier: Int = 3

multiplyByN(3)(3)
multiplyByN(2)
multiplyByN(1)

/**
 * [User(1, "User A"), User(2, "User B"), User(3, "User B")]
 *
 * Function
 * User => UserName
 *
 * Iteraciones
 * initial =  [], acc = [], current = User(1, "User A")
 * acc = [User(1, "User A")], current = User(2, "User B")
 * acc = [A, B], current = User(3, "User B")
 */




implicit class ListExtension[T](targetList: List[T]) {
  def distinctOnProp[A](fn: T => A): List[T] = {
    targetList.foldLeft(List.empty[T]) { (acc, current) => {
      val value = fn(current)
      acc.find(item => fn(item) == value) match {
        case Some(_) => acc // retornar acumulador
        case None => current :: acc // agregamos al acumulador
      }
    }
    }
  }
}

case class User(id: Int, username: String)

def getUserName: User => String = user => user.username

val list = List(
  User(1, "User A"),
  User(2, "User B"),
  User(3, "User B")
)

list.distinctOnProp(getUserName)






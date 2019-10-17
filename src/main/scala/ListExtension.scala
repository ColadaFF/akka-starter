class ListExtension[T](targetList: List[T]) {
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
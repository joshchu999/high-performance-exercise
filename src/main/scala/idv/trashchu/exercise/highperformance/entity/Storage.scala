package idv.trashchu.exercise.highperformance.entity

import java.util.UUID

/**
  * Created by Joshchu on 2016/7/23.
  */
case class User(userID: UUID, x: Int, y: Int)
case class Monster(monsterID: UUID, categoryID: Int, x: Int, y: Int)
case class Pet(userID: UUID, monsterID: UUID, categoryID: Int)
package com.example.cori.utils

import kotlin.reflect.KClass

fun checkThread(
  where : KClass<*>,
) {
  val thread = Thread.currentThread()
  println("thread: ${thread.name} in ${where.simpleName}")
}
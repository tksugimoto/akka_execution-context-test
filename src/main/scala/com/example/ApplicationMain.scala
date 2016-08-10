package com.example

import akka.actor.ActorSystem
import com.typesafe.config.ConfigFactory

import scala.concurrent.{Future, ExecutionContextExecutor}

object ApplicationMain extends App {
  
  val customConf = ConfigFactory.parseString("""
      akka.actor {
        default-dispatcher {
          default-executor {
            fallback = "thread-pool-executor"
          }
          thread-pool-executor {
            # キューの上限なし
            task-queue-size = -1
            
            
            # Minimum number of threads to cap factor-based max number to
            # (if using a bounded task queue)            
            max-pool-size-min = 8
            
            # Max no of threads (if using a bounded task queue) is determined by
            # calculating: ceil(available processors * factor)
            max-pool-size-factor  = 3.0
            
            # Max number of threads to cap factor-based max number to
            # (if using a  bounded task queue)
            max-pool-size-max = 10
          }
        }
      }
  """)
  
  val system = ActorSystem("MyActorSystem", ConfigFactory.load(customConf))
  
  implicit val executionContext: ExecutionContextExecutor = system.dispatcher
    
  println("foreach: start")
  (1 to 50).foreach { n =>
    Future {
      println(s"start: ${n}")
      Thread.sleep(3000)
      println(s"end: ${n}")
    }
  }
  println("foreach: end")

  system.awaitTermination()
}

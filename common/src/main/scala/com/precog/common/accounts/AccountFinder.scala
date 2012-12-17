/*
 *  ____    ____    _____    ____    ___     ____ 
 * |  _ \  |  _ \  | ____|  / ___|  / _/    / ___|        Precog (R)
 * | |_) | | |_) | |  _|   | |     | |  /| | |  _         Advanced Analytics Engine for NoSQL Data
 * |  __/  |  _ <  | |___  | |___  |/ _| | | |_| |        Copyright (C) 2010 - 2013 SlamData, Inc.
 * |_|     |_| \_\ |_____|  \____|   /__/   \____|        All Rights Reserved.
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the 
 * GNU Affero General Public License as published by the Free Software Foundation, either version 
 * 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See 
 * the GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License along with this 
 * program. If not, see <http://www.gnu.org/licenses/>.
 *
 */
package com.precog.common
package accounts

import com.precog.common.security._

import scalaz._
import scalaz.syntax.monad._

trait AccountFinder[M[+_]] {
  implicit val M: Monad[M]

  def findAccountById(accountId: AccountId): M[Option[Account]]

  def listAccountIds(apiKey: APIKey) : M[Option[AccountId]]

  def mapAccountIds(apiKeys: Set[APIKey]) : M[Map[APIKey, AccountId]] = {
    apiKeys.foldLeft(Map.empty[APIKey, AccountId].point[M]) {
      case (macc, key) => 
        for {
          m <- macc
          ids <- listAccountIds(key)
        } yield {
          m ++ ids.map(key -> _)
        }
    }
  }
}


// vim: set ts=4 sw=4 et:
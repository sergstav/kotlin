/*
 * Copyright 2010-2014 JetBrains s.r.o.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

class Some {
    var a : Int
        get() = 0
        <caret>
}

// EXIST:  abstract
// EXIST:  annotation
// EXIST:  as
// EXIST:  class
// EXIST:  enum
// EXIST:  final
// EXIST:  fun
// EXIST:  get
/*TODO*/
// EXIST:  in
/*why?*/
// EXIST:  inner
// EXIST:  internal
// EXIST:  object
// EXIST:  open
// EXIST:  out
/*why?*/
// EXIST:  override
// EXIST:  private
// EXIST:  protected
// EXIST:  public
// EXIST: set
// EXIST:  trait
// EXIST:  val
// EXIST:  var
// EXIST:  vararg
/*why?*/
// NUMBER: 23
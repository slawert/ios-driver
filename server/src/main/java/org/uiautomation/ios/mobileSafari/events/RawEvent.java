/*
 * Copyright 2012 ios-driver committers.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package org.uiautomation.ios.mobileSafari.events;

import org.json.JSONObject;

public class RawEvent implements Event {

  private final JSONObject raw;
  private final long timestamp;

  public RawEvent(JSONObject raw) {
    this.raw = raw;
    timestamp = System.currentTimeMillis();
  }

  @Override
  public long getAge() {
    return System.currentTimeMillis() - timestamp;
  }

  @Override
  public String toString() {
    return raw.optString("method");
  }
}

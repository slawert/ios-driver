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
package org.uiautomation.ios.server.command.uiautomation;

import org.json.JSONException;
import org.json.JSONObject;
import org.uiautomation.ios.communication.WebDriverLikeRequest;
import org.uiautomation.ios.server.IOSDriver;
import org.uiautomation.ios.server.command.UIAScriptHandler;


public class ClearNHandler extends UIAScriptHandler {

  private static final String voidTemplate =
      "var element = UIAutomation.cache.get(:reference);" +
      "element.setValue('');" +
      "UIAutomation.createJSONResponse(':sessionId',0,'')";

  public ClearNHandler(IOSDriver driver, WebDriverLikeRequest request) {
    super(driver, request);
    String js = voidTemplate
        .replace(":sessionId", request.getSession())
        .replace(":reference", request.getVariableValue(":reference"));
    setJS(js);

  }

  @Override
  public JSONObject configurationDescription() throws JSONException {
    return noConfigDefined();
  }
}


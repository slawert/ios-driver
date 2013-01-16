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
package org.uiautomation.ios.webInspector.DOM;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.uiautomation.ios.mobileSafari.DebugProtocol;
import org.uiautomation.ios.mobileSafari.IosAtoms;
import org.uiautomation.ios.mobileSafari.NodeId;
import org.uiautomation.ios.server.ServerSideSession;

import java.util.List;

public class RemoteObject {

  private final String objectId;
  private final ServerSideSession session;
  private final DebugProtocol protocol;

  public RemoteObject(String objectId, ServerSideSession session) throws JSONException {
    this.session = session;
    this.protocol = session.getWebInspector().getProtocol();
    this.objectId = objectId;
  }

  public DebugProtocol getProtocol() {
    return protocol;
  }

  public String getId() {
    return objectId;
  }

  public RemoteWebElement getWebElement() throws JSONException, Exception {
    JSONObject result = protocol.sendCommand(DOM.requestNode(objectId));
    int id = result.getInt("nodeId");
    NodeId nodeId = new NodeId(id);
    return new RemoteWebElement(nodeId, session, this);
  }

  public List<RemoteObject> flatten() throws Exception {
    if (!isArray()) {
      throw new RuntimeException("Not an array. Cannot be flattened.");
    }
    JSONObject cmd = new JSONObject();
    cmd.put("method", "Runtime.callFunctionOn");

    cmd.put(
        "params",
        new JSONObject()
            .put("objectId", this.getId())
            .put(
                "functionDeclaration",
                "(function(arg) { " +
                "var array = this;" +
                "for (var name in this[0]){alert(name+':'+this[0][name])}" +
                //"var length=array.length;" +
                //"var res = '';" +
                //"for(var i = 0;i<length;i++)" +
                //"{res+='||'+array[i].id;}; " +
                "return res;})")
            .put("returnByValue", false));

    JSONObject response = protocol.sendCommand(cmd);
    return session.getWebInspector().cast(response);
  }

  private boolean isArray() {
    // TODO Auto-generated method stub
    return true;
  }

  @Override
  public String toString() {
    return objectId;
  }

  public <T> T call(String function) throws Exception {
    JSONObject cmd = new JSONObject();
    cmd.put("method", "Runtime.callFunctionOn");

    JSONArray args = new JSONArray();
    args.put(new JSONObject().put("value", ""));

    cmd.put(
        "params",
        new JSONObject().put("objectId", this.getId())
            .put("functionDeclaration",
                 "(function(arg) { var res = this" + function + "; return res;})")
            .put("arguments", args).put("returnByValue", false));

    JSONObject response = protocol.sendCommand(cmd);
    return session.getWebInspector().cast(response);

  }

  public String stringify() throws Exception {
    JSONObject cmd = new JSONObject();
    cmd.put("method", "Runtime.callFunctionOn");

    JSONArray args = new JSONArray();

    cmd.put(
        "params",
        new JSONObject().put("objectId", this.getId())
            .put("functionDeclaration",
                 "(function() { var res = " + IosAtoms.STRINGIFY + "(this); return res;})")
            .put("arguments", args).put("returnByValue", true));

    JSONObject response = protocol.sendCommand(cmd);
    return session.getWebInspector().cast(response);
  }

  public String getTextAreaValue() throws Exception {
    JSONObject cmd = new JSONObject();
    cmd.put("method", "Runtime.callFunctionOn");

    JSONArray args = new JSONArray();
    args.put(new JSONObject().put("value", ""));

    cmd.put(
        "params",
        new JSONObject().put("objectId", this.getId())
            .put("functionDeclaration", "(function(arg) { var res = this.value; return res;})")
            .put("arguments", args)
            .put("returnByValue", true));

    JSONObject response = protocol.sendCommand(cmd);
    return session.getWebInspector().cast(response);
  }
}

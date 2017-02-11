/**
 *  Z-Wave Garage Door Opener
 *
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License. You may obtain a copy of the License at:
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software distributed under the License is distributed
 *  on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License
 *  for the specific language governing permissions and limitations under the License.
 *
 */
metadata {
  definition (name: "Virtual Garage Door", namespace: "bbrouwer", author: "Bruce Brouwer") {
    capability "Actuator"
    capability "Door Control"
    capability "Garage Door Control"
    capability "Contact Sensor"
    capability "Momentary"
    capability "Refresh"
    capability "Sensor"
  }

  preferences {
    input "button", "number", title: "button", required: true, multiple: false
  }
  
  	simulator {
		status "on":  "command: 2003, payload: FF"
		status "off": "command: 2003, payload: 00"

		// reply messages
		reply "2001FF,2502,delay 1000,200100,2502": "command: 2503, payload: FF"
		reply "200100,2502": "command: 2503, payload: 00"
	}

}

def parse(String description) {
    def result = null
    def cmd = zwave.parse(description, [0x20: 1])
    if (cmd) {
       result = createEvent(zwaveEvent(cmd))
    }
    log.debug "Parse returned ${result?.descriptionText}"
    return result
}

def open() {
    sendEvent(name: "door", value: "opening")
    runIn(6, finishOpening)
}

def close() {
    sendEvent(name: "door", value: "closing")
    runIn(6, finishClosing)
}

def finishOpening() {
    sendEvent(name: "door", value: "open")
    sendEvent(name: "contact", value: "open")
}

def finishClosing() {
    sendEvent(name: "door", value: "closed")
    sendEvent(name: "contact", value: "closed")
}

def refresh() {

}
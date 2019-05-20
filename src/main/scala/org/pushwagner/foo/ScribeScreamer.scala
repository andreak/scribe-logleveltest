package org.pushwagner.foo

import com.visena.logtest.Loggable

class ScribeScreamer extends Loggable {

	def scream(): Unit = {
		info("Info.")
		warn("Warn..")
		error("Error!")
	}
}

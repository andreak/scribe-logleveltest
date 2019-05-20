package org.pushwagner.foo

import com.visena.logtest.Loggable

class ScribeOutsideValidPackagesNonCranker extends Loggable {

	def dontCrank(): Unit = {
		debug("Entering dontCrank...")
		info("zzzzz")
	}
}

package no.officenet.crank

import com.visena.logtest.Loggable

class ScribeCranker extends Loggable {

	def doCrank(): Unit = {
		trace("Entering doCrank...")
		debug("Preparing...")
		info("Crank it up!")
		warn("Warning!!!")
		error("Error!!!")
	}
}

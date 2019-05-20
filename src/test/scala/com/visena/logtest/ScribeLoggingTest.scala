package com.visena.logtest

import no.officenet.crank.ScribeCranker
import org.pushwagner.foo.{ScribeOutsideValidPackagesNonCranker, ScribeScreamer}
import org.testng.annotations.{BeforeClass, Test}
import scribe.Level
import scribe.filter.{className, level, packageName, select}
import scribe.writer.ConsoleWriter

@Test
class ScribeLoggingTest extends Loggable {

	@BeforeClass
	def beforeClass(): Unit = {
		scribe.Logger.root
			.clearHandlers()
			.clearModifiers()
			.withHandler(minimumLevel = Some(Level.Info)
				, writer = ConsoleWriter
			)
			.withModifier(
				select(packageName.startsWith("no.officenet")
					, packageName.startsWith("com.visena")
				).boosted(Level.Debug, Level.Info))
			.withModifier(select(className(classOf[ScribeScreamer].getName)).include(level >= Level.Error))
			.replace()
	}

	def testPackageFiltering(): Unit = {
		trace("Starting test") // This should be printed out to stdout with TRACE
		new ScribeCranker().doCrank() // Should print TRACE and INFO messages
		new ScribeOutsideValidPackagesNonCranker().dontCrank() // Should only print INFO
		new ScribeScreamer().scream()
	}

}

package com.visena.logtest

import java.nio.file.Paths

import no.officenet.crank.ScribeCranker
import org.pushwagner.foo.{ScribeOutsideValidPackagesNonCranker, ScribeScreamer}
import org.testng.annotations.{BeforeClass, Test}
import scribe.filter.{packageName, select}
import scribe.handler.LogHandler
import scribe.writer.file.LogPath
import scribe.writer.{ConsoleWriter, FileWriter}
import scribe.{Level, Logger}

@Test
class ScribeLoggingTest extends Loggable {

	@BeforeClass
	def beforeClass(): Unit = {
		val moduleName = "logtest"

		val consoleHandler = LogHandler(minimumLevel = Some(Level.Info), writer = ConsoleWriter)
		val traceHandler = LogHandler(
			minimumLevel = Some(Level.Info),
			writer = FileWriter()
				.path(_ => Paths.get(System.getProperty("user.home"),"logs", moduleName, "trace.log"))
				.rolling(LogPath.daily(prefix = "trace"
					, directory = Paths.get(System.getProperty("user.home"), "logs", moduleName))
				).autoFlush
		).withModifier(
			select(
				packageName.startsWith("no.officenet"),
				packageName.startsWith("com.visena")
			).boosted(Level.Trace, Level.Info)
		)

		Logger.root
			.clearHandlers()
			.clearModifiers()
			.withHandler(consoleHandler)
			.withHandler(traceHandler).replace()
	}

	def testPackageFiltering(): Unit = {
		trace("Starting test") // This should be printed out to stdout with TRACE
		new ScribeCranker().doCrank() // Should print TRACE and INFO messages
		new ScribeOutsideValidPackagesNonCranker().dontCrank() // Should only print INFO
		new ScribeScreamer().scream()
	}

}

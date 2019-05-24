package com.visena.logtest

import java.nio.file.Paths

import no.officenet.crank.ScribeCranker
import org.pushwagner.foo.{ScribeOutsideValidPackagesNonCranker, ScribeScreamer}
import org.testng.annotations.{BeforeClass, Test}
import scribe.filter.{packageName, className, level, select}
import scribe.handler.LogHandler
import scribe.writer.file.LogPath
import scribe.writer.{ConsoleWriter, FileWriter}
import scribe.{Level, Logger, Priority}

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
			).boosted(Level.Trace, Level.Info).priority(Priority.Important)
		).withModifier(
			select(
				className(classOf[ScribeScreamer].getName)
			).include(level >= Level.Error)
		)

		val warnHandler = LogHandler(
			writer = FileWriter()
				.path(_ => Paths.get(System.getProperty("user.home"),"logs", moduleName, "warn.log"))
				.rolling(LogPath.daily(prefix = "warn"
					, directory = Paths.get(System.getProperty("user.home"), "logs", moduleName))
				).autoFlush
		).withModifier(
			select(
				packageName.startsWith("no.officenet"),
				packageName.startsWith("com.visena")
			).exclude(level < Level.Warn).exclude(level > Level.Warn).excludeUnselected
		)

		val errorHandler = LogHandler(
			minimumLevel = Some(Level.Error),
			writer = FileWriter()
				.path(_ => Paths.get(System.getProperty("user.home"),"logs", moduleName, "error.log"))
				.rolling(LogPath.daily(prefix = "error"
					, directory = Paths.get(System.getProperty("user.home"), "logs", moduleName))
				).autoFlush
		)

		Logger.root
			.clearHandlers()
			.clearModifiers()
			.withHandler(consoleHandler)
			.withHandler(traceHandler)
			.withHandler(warnHandler)
			.withHandler(errorHandler)
			.replace()
	}

	def testPackageFiltering(): Unit = {
		trace("Starting test") // This should be printed out to stdout with TRACE
		new ScribeCranker().doCrank() // Should print TRACE and INFO messages
		new ScribeOutsideValidPackagesNonCranker().dontCrank() // Should only print INFO
		new ScribeScreamer().scream()
	}

}

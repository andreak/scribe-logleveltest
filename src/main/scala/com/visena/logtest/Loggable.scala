package com.visena.logtest

import scribe.{LogRecord, Logger, LoggerSupport}

trait Loggable extends LoggerSupport {
	@transient
	val myLogger = this

	override def log[M](record: LogRecord[M]): Unit = Logger(record.className).log(record)
}

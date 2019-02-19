package com.kamilh.findmysong.extensions

import android.text.format.DateUtils
import org.threeten.bp.LocalDate
import org.threeten.bp.OffsetDateTime
import org.threeten.bp.format.DateTimeFormatter

private val localDateFormatter = DateTimeFormatter.ISO_LOCAL_DATE
private val offsetDateTimeFormatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME

fun OffsetDateTime.toRelative() = DateUtils.getRelativeTimeSpanString(this.toEpochSecond(), OffsetDateTime.now().toEpochSecond(), DateUtils.SECOND_IN_MILLIS)

fun OffsetDateTime.toIsoString() = format(offsetDateTimeFormatter)

fun String.toOffsetDateTime(): OffsetDateTime? = if (isEmpty()) null else OffsetDateTime.parse(this, offsetDateTimeFormatter)

fun LocalDate.toIsoString() = format(localDateFormatter)

fun String.toDateTime(): LocalDate? = if (isEmpty()) null else LocalDate.parse(this, localDateFormatter)

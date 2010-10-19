package scaladbtest.model

/*
* Copyright 2010 Ken Egervari
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
* http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

class Record(var table: Table, val label: String, val columns: List[Column]) {

	def this(label: String, columns: List[Column] = List()) = this(null, label, columns)

	def commaSeparatedString(columnToString: Column => String): String = {
		columns.tail.foldLeft(columnToString(columns.head))(_ + ", " + columnToString(_))
	}
	
	def commaSeparatedColumnNames: String = {
		commaSeparatedString(_.name)
	}

	def commaSeparatedColumnValues: String = {
		commaSeparatedString(_.value.sqlValue)
	}

	def insertSql = {

		new StringBuilder()
			.append("INSERT INTO ")
			.append(table.name)
			.append("(")
			.append(commaSeparatedColumnNames)
			.append(") VALUES(")
			.append(commaSeparatedColumnValues)
			.append(");")
			.toString
	}

	def insert(): Unit = {
		table.testData.jdbcTemplate.update(insertSql)
	}

	override def toString = {
		val tableName =
			if(table != null) table.name
			else "N/A"

		"Record(Table(" + tableName + ")," + label + "," + columns + ")"
	}
}
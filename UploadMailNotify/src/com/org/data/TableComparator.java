package com.org.data;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;

import javax.sql.rowset.CachedRowSet;

import com.org.upload.ConnectionProvider;

public class TableComparator {

	public static TableInfo getTableDetails(ResultSetMetaData rsmd,
			String tbName, String pkey) throws SQLException {
		System.out
				.println("*****************************************************************************************");
		System.out.println("No of Columns : " + rsmd.getColumnCount());
		System.out
				.println("*****************************************************************************************");
		System.out.println("Name\t\t\t Type\t\t Size \t\t Nullable");
		TableInfo ti = new TableInfo();
		for (int i = 1; i <= rsmd.getColumnCount(); i++) {
			System.out.println(rsmd.getColumnName(i) + "\t\t\t"
					+ rsmd.getColumnTypeName(i) + "\t\t" + rsmd.getPrecision(i)
					+ "\t\t" + ((rsmd.isNullable(i) == 1) ? "Yes" : "No"));

			ColumnInfo ci = new ColumnInfo();
			ci.setColName(rsmd.getColumnName(i));
			ci.setColType(rsmd.getColumnTypeName(i));
			ci.setSize(rsmd.getPrecision(i));
			ci.setNullable((rsmd.isNullable(i) == 1));
			ti.getColumnList().add(ci);
		}
		ti.setTbName(tbName);
		ti.setPkey(pkey);
		return ti;
	}

	public static TableStructureDiff compareTableStructure(TableInfo ti1,
			TableInfo ti2) {
		ArrayList<String> columnDiffList = new ArrayList<String>();
		ArrayList<String> newColumnList = new ArrayList<String>();
		ArrayList<String> removedColumnList = new ArrayList<String>();
		ArrayList<String> matchingCols = new ArrayList<String>();
		ArrayList<DDLScript> colLevelDDLScript = new ArrayList<DDLScript>();
		for (ColumnInfo ci : ti1.columnList) {
			String msg = null;
			for (ColumnInfo ci1 : ti2.columnList) {
				if (ci.getColName().equalsIgnoreCase(ci1.getColName())) {
					msg = "";
					if (!ci.getColType().equalsIgnoreCase(ci1.getColType())) {
						msg += "[Column Type Diff : Source : "
								+ ci.getColType() + " Destination : "
								+ ci1.getColType() + " and Column Name : "
								+ ci.getColName() + "]";
					}
					if (!(ci.getSize() == ci1.getSize())) {
						msg += "[Column Size Diff : Source : " + ci.getSize()
								+ " Destination : " + ci1.getSize()
								+ " and Column Name : " + ci.getColName() + "]";
					}
					if (!(ci.isNullable() == ci1.isNullable())) {
						msg += "[Column Nullable Diff : Source : "
								+ ((ci.isNullable()) ? "Yes" : "No")
								+ " Destination : "
								+ ((ci1.isNullable()) ? "Yes" : "No")
								+ " and Column Name : " + ci.getColName() + "]";
					}
					if (msg.length() > 0) {
						columnDiffList.add("Modify Column - " + msg);
						// /added for getting Col level DDL script ::BEGIN::
						DDLScript colLevelDDL = new DDLScript(ti2.getTbName(),
								ci, 'A');
						colLevelDDLScript.add(colLevelDDL);
						// /added for getting Col level DDL script ::END::
					}
					matchingCols.add(ci.getColName());
					break;
				}
			}
			if (msg == null) {
				msg = "[New Column Name : " + ci.getColName() + " Type : "
						+ ci.getColType() + " Size : " + ci.getSize()
						+ " Nullable : " + ((ci.isNullable()) ? "Yes" : "No")
						+ "]";
				newColumnList.add(msg);
				// /added for getting Col level DDL script ::BEGIN::
				DDLScript colLevelDDL = new DDLScript(ti2.getTbName(), ci, 'C');
				colLevelDDLScript.add(colLevelDDL);
				// /added for getting Col level DDL script ::END::
			}

		}

		for (ColumnInfo ci1 : ti2.columnList) {

			boolean test = false;
			for (ColumnInfo ci : ti1.columnList) {
				if (ci1.getColName().equalsIgnoreCase(ci.getColName())) {
					test = true;
					break;
				}
			}
			if (!test) {
				String msg = "[Removed Column Name : " + ci1.getColName()
						+ " Type : " + ci1.getColType() + " Size : "
						+ ci1.getSize() + " Nullable : "
						+ ((ci1.isNullable()) ? "Yes" : "No") + "]";
				newColumnList.add(msg);
				// /added for getting Col level DDL script::BEGIN::
				DDLScript colLevelDDL = new DDLScript(ti2.getTbName(), ci1, 'D');
				colLevelDDLScript.add(colLevelDDL);
				// /added for getting Col level DDL script ::END::
			}
		}
		TableStructureDiff tsd = new TableStructureDiff(columnDiffList,
				newColumnList, removedColumnList, ti1, ti2, matchingCols,
				colLevelDDLScript);
		System.out.println(columnDiffList);
		System.out.println(newColumnList);
		System.out.println(removedColumnList);
		tsd.setTi1(ti1);
		tsd.setTi2(ti2);
		return tsd;
	}

	public static GenScriptsList compareTableData(ResultSet rs, ResultSet rs1,
			TableStructureDiff tsd) throws SQLException {
		int noOfCompare = 0;
		ArrayList<String> matchingCols = tsd.getMacthingCols();
		ArrayList<String> newColList = tsd.getTi1().getAllColNames();
		newColList.removeAll(matchingCols);

		ArrayList<String> insertScripts = new ArrayList<String>();
		ArrayList<String> updateScripts = new ArrayList<String>();
		ArrayList<String> deleteScripts = new ArrayList<String>();
		String pkey = tsd.getTi1().getPkey();
		// //finding info regarding new column data to be added or not Y/N
		String tableName = tsd.getTi1().getTbName();
		String tbList[] = ConnectionProvider.p.getProperty("tablelist").split(
				"\\|");
		int k = 0;
		for (k = 0; k < tbList.length; k++) {
			if (tbList[k].equals(tableName))
				break;
		}
		String nwColCondList[] = ConnectionProvider.p.getProperty("newCols")
				.split("\\|");
		String nwColCond = nwColCondList[k];
		// ///end of finding Y/N for new column data to be added

		String pkeycols[] = pkey.split(",");
		// System.out.println(pkeycols[3]);

		// ******/for creating insert script, if target table having no records
		// at all
		if (rs.first()) {
			if (!rs1.first()) {
				rs.beforeFirst();
				for (; rs.next();) {
					StringBuilder colString = new StringBuilder();
					StringBuilder valString = new StringBuilder();
					boolean comma = false;
					for (String colName : matchingCols) {
						// System.out.println(rs.getObject(colName)+" \t "+rs1.getObject(colName));
						// if(!(rs.getObject(colName)+"").equals(rs1.getObject(colName)+""))

						if (comma) {
							colString = colString.append(",");
							valString = valString.append(",");
						}
						comma = true;
						colString = colString.append(" " + colName + " ");
						if (rs.getObject(colName) == null)
							valString = valString.append("null");
						else
							valString = valString.append("'"
									+ rs.getObject(colName) + "'");

					}

					// //for new column data to be added in scripts . appending
					// new column data
					if (nwColCond.equals("Y")) {
						for (String colName : newColList) {
							// System.out.println(rs.getObject(colName)+" \t "+rs1.getObject(colName));
							if (comma) {
								colString = colString.append(",");
								valString = valString.append(",");
							}
							comma = true;
							colString = colString.append(" " + colName + " ");
							if (rs.getObject(colName) == null)
								valString = valString.append("null");
							else
								valString = valString.append("'"
										+ rs.getObject(colName) + "'");

						}
					}
					// //end of getting new column data in scripts

					String str = "INSERT INTO " + tsd.ti2.getTbName() + "  ("
							+ colString + ") VALUES  (" + valString + ");";
					// System.out.println(str);
					insertScripts.add(str);
				}
			}

		}
		rs.beforeFirst();
		rs1.beforeFirst();

		// ///////////////////////////////

		for (; rs.next();) {
			// System.out.println("?????????????????");

			if (rs1.first()) {

				boolean checkExist = false;
				boolean test = true;
				for (int i = 0; test; i++) {
					// System.out.println("------------********");
					boolean pkmatch = false;
					int counter = 0;

					for (String colName : pkeycols) {
						noOfCompare++;
						// System.out.println(rs.getObject(colName).toString()+"\t"+rs1.getObject(colName).toString());
						if (rs.getObject(colName).toString()
								.equals(rs1.getObject(colName).toString())) {
							// System.out.println("**************"+colName);
							counter++;

						}
					}
					// System.out.println("Its working....."+counter);
					// System.out.println(pkeycols.length+" lemgth of pkeycols and counter "+counter);
					if (counter == pkeycols.length) {

						System.out.println("Its getting match.....");
						StringBuilder setString = new StringBuilder();
						StringBuilder condString = new StringBuilder();
						boolean comma = false;
						for (String colName : matchingCols) {
							noOfCompare++;
							// System.out.println(rs.getObject(colName)+" \t "+rs1.getObject(colName));
							if (!(rs.getObject(colName) + "").equals(rs1
									.getObject(colName) + "")) {
								if (comma)
									setString = setString.append(",");
								comma = true;

								if (rs.getObject(colName) == null)
									setString = setString.append(" " + colName
											+ "=null ");
								else
									setString = setString.append(" " + colName
											+ "='" + rs.getObject(colName)
											+ "' ");

							}
						}
						// //for new column data to be added in scripts .
						// appending new column data
						if (nwColCond.equals("Y")) {
							for (String colName : newColList) {
								// System.out.println(rs.getObject(colName)+" \t "+rs1.getObject(colName));
								if (comma)
									setString = setString.append(",");
								comma = true;

								if (rs.getObject(colName) == null)
									setString = setString.append(" " + colName
											+ "=null ");
								else
									setString = setString.append(" " + colName
											+ "='" + rs.getObject(colName)
											+ "' ");

							}
						}
						// //end of getting new column data in scripts

						boolean andCond = false;
						for (String colName : pkeycols) {
							if (andCond)
								condString = condString.append(" AND ");
							andCond = true;
							if (rs.getObject(colName) == null)
								condString = condString.append(colName
										+ " IS NULL ");
							else
								condString = condString.append(colName + "='"
										+ rs.getObject(colName) + "'");

						}
						// System.out.println(setString.length()+"   "+condString.length());
						if (setString.length() > 1 && condString.length() > 1) {
							String str = "UPDATE " + tsd.ti2.getTbName()
									+ " SET " + setString + " WHERE "
									+ condString + " ;";
							// System.out.println(str);
							updateScripts.add(str);
						}
						checkExist = true;
						break;
					}
					test = rs1.next();
				}

				if (!checkExist) {
					StringBuilder colString = new StringBuilder();
					StringBuilder valString = new StringBuilder();
					boolean comma = false;
					for (String colName : matchingCols) {
						// System.out.println(rs.getObject(colName)+" \t "+rs1.getObject(colName));
						// if(!(rs.getObject(colName)+"").equals(rs1.getObject(colName)+""))

						if (comma) {
							colString = colString.append(",");
							valString = valString.append(",");
						}
						comma = true;
						colString = colString.append(" " + colName + " ");
						if (rs.getObject(colName) == null)
							valString = valString.append("null");
						else
							valString = valString.append("'"
									+ rs.getObject(colName) + "'");

					}

					// //for new column data to be added in scripts . appending
					// new column data
					if (nwColCond.equals("Y")) {
						for (String colName : newColList) {
							// System.out.println(rs.getObject(colName)+" \t "+rs1.getObject(colName));
							if (comma) {
								colString = colString.append(",");
								valString = valString.append(",");
							}
							comma = true;
							colString = colString.append(" " + colName + " ");
							if (rs.getObject(colName) == null)
								valString = valString.append("null");
							else
								valString = valString.append("'"
										+ rs.getObject(colName) + "'");

						}
					}
					// //end of getting new column data in scripts

					String str = "INSERT INTO " + tsd.ti2.getTbName() + "  ("
							+ colString + ") VALUES  (" + valString + ");";
					// System.out.println(str);
					insertScripts.add(str);
				}
			}
		}
		System.out.println(new Date() + "mmmmmmmmmm");
		rs1.beforeFirst();

		for (; rs1.next();) {
			// System.out.println("?????????????????");

			if (rs.first()) {
				boolean checkExist = false;
				boolean test = true;
				for (int i = 0; test; i++) {
					// System.out.println("------------********");
					boolean pkmatch = false;
					int counter = 0;

					for (String colName : pkeycols) {
						noOfCompare++;
						// System.out.println(rs.getObject(colName).toString()+"\t"+rs1.getObject(colName).toString());
						if (rs1.getObject(colName).toString()
								.equals(rs.getObject(colName).toString())) {
							counter++;
						}
					}
					// System.out.println(pkeycols.length+" lemgth of pkeycols and counter "+counter);
					if (counter == pkeycols.length) {
						checkExist = true;
						break;
					}
					test = rs.next();
				}
				if (!checkExist) {
					StringBuilder condString = new StringBuilder();
					boolean andCond = false;
					for (String colName : pkeycols) {
						if (andCond)
							condString = condString.append(" AND ");
						andCond = true;

						if (rs1.getObject(colName) == null)
							condString = condString.append(colName
									+ " IS NULL ");
						else
							condString = condString.append(colName + "='"
									+ rs1.getObject(colName) + "' ");

					}
					String str = "DELETE FROM " + tsd.ti2.getTbName()
							+ " WHERE " + condString + " ;";
					// System.out.println(str);
					deleteScripts.add(str);

				}
			}
		}

		System.out.println(new Date() + "lllllllllll");
		GenScriptsList gsl = new GenScriptsList(deleteScripts, insertScripts,
				updateScripts);
		gsl.setTableName(tsd.ti2.getTbName());
		System.out.println("No of Compare : " + noOfCompare);
		return gsl;
	}

	public static GenScriptsList compareTableDataEnhance(ResultSet rs,
			ResultSet rs1, TableStructureDiff tsd, int countRows, int countRows1)
			throws SQLException {

		int noOfCompare = 0;
		ArrayList<String> matchingCols = tsd.getMacthingCols();
		ArrayList<String> newColList = tsd.getTi1().getAllColNames();
		newColList.removeAll(matchingCols);

		ArrayList<String> insertScripts = new ArrayList<String>();
		ArrayList<String> updateScripts = new ArrayList<String>();
		ArrayList<String> deleteScripts = new ArrayList<String>();
		String pkey = tsd.getTi1().getPkey();
		// //finding info regarding new column data to be added or not Y/N
		String tableName = tsd.getTi1().getTbName();
		String tbList[] = ConnectionProvider.p.getProperty("tablelist").split(
				"\\|");

		int k = 0;
		for (k = 0; k < tbList.length; k++) {
			if (tbList[k].equals(tableName))
				break;
		}

		String nwColCondList[] = ConnectionProvider.p.getProperty("newCols")
				.split("\\|");

		// ///////////////////1111111
		System.out.println("?????" + nwColCondList.length);
		String nwColCond = null;
		if (k >= nwColCondList.length)
			nwColCond = "N";
		else
			nwColCond = nwColCondList[k];
		// ///end of finding Y/N for new column data to be added

		String pkeycols[] = pkey.split(",");
		// System.out.println(pkeycols[3]);

		// ///////////////////////////////
		// for using binary search , To get total no of records
		// rs1.last();
		int totalrowsCount = countRows1;// rs.getRow();//
		System.out.println(new Date());
		/*
		 * for(;rs1.next();) { System.out.println(totalrowsCount++); }
		 */

		System.out.println("**************" + totalrowsCount);

		// ******/for creating insert script, if target table having no records
		// at all
		if (rs.first()) {
			if (!rs1.first()) {
				rs.beforeFirst();
				for (; rs.next();) {
					StringBuilder colString = new StringBuilder();
					StringBuilder valString = new StringBuilder();
					boolean comma = false;
					for (String colName : matchingCols) {
						// System.out.println(rs.getObject(colName)+" \t "+rs1.getObject(colName));
						// if(!(rs.getObject(colName)+"").equals(rs1.getObject(colName)+""))

						if (comma) {
							colString = colString.append(",");
							valString = valString.append(",");
						}
						comma = true;
						colString = colString.append(" " + colName + " ");
						if (rs.getObject(colName) == null)
							valString = valString.append("null");
						else
							valString = valString.append("'"
									+ rs.getObject(colName) + "'");

					}

					// //for new column data to be added in scripts . appending
					// new column data
					if (nwColCond.equals("Y")) {
						for (String colName : newColList) {
							// System.out.println(rs.getObject(colName)+" \t "+rs1.getObject(colName));
							if (comma) {
								colString = colString.append(",");
								valString = valString.append(",");
							}
							comma = true;
							colString = colString.append(" " + colName + " ");
							if (rs.getObject(colName) == null)
								valString = valString.append("null");
							else
								valString = valString.append("'"
										+ rs.getObject(colName) + "'");

						}
					}
					// //end of getting new column data in scripts

					String str = "INSERT INTO " + tsd.ti2.getTbName() + "  ("
							+ colString + ") VALUES  (" + valString + ");";
					// System.out.println(str);
					insertScripts.add(str);
				}

				GenScriptsList gsl = new GenScriptsList(deleteScripts,
						insertScripts, updateScripts);
				gsl.setTableName(tsd.ti2.getTbName());
				System.out.println("No of Compare : " + noOfCompare);
				return gsl;
			}

		}
		rs.beforeFirst();
		rs1.beforeFirst();
		for (; rs.next();) {

			if (rs1.first()) {

				// /////////////////////////////////////////////////////////////////////////////
				// trying to use binary search

				int min = 1;
				int high = totalrowsCount;

				boolean checkExist = false;
				while (min <= high) {
					int mid = (high + min) / 2;
					// System.out.println("**************");
					rs1.first();
					if (rs1.relative(mid - 1)) {
						int counter = 0;
						for (String colName : pkeycols) {

							// System.out.println(colName+" : check match : "+rs.getObject(colName).toString()+"\t"+rs1.getObject(colName).toString());
							if ((rs.getObject(colName) + "").equals((rs1
									.getObject(colName) + ""))) {
								// System.out.println("**************"+colName);
								counter++;
							}
						}
						// System.out.println("??????????");
						// System.out.println("Its working....."+counter);
						if (counter == pkeycols.length) {
							// System.out.println("Its getting match...."+counter);
							// //written for adding insert and update scripts

							StringBuilder setString = new StringBuilder();
							StringBuilder condString = new StringBuilder();
							boolean comma = false;
							for (String colName : matchingCols) {
								noOfCompare++;
								// System.out.println(rs.getObject(colName)+" \t "+rs1.getObject(colName));
								if (!(rs.getObject(colName) + "").equals(rs1
										.getObject(colName) + "")) {
									if (comma)
										setString = setString.append(",");
									comma = true;
									if (rs.getObject(colName) == null)
										setString = setString.append(" "
												+ colName + "=null ");
									else
										setString = setString.append(" "
												+ colName + "='"
												+ rs.getObject(colName) + "' ");

								}
							}
							// //for new column data to be added in scripts .
							// appending new column data
							if (nwColCond.equals("Y")) {
								for (String colName : newColList) {
									// System.out.println(rs.getObject(colName)+" \t "+rs1.getObject(colName));
									if (comma)
										setString = setString.append(",");
									comma = true;
									if (rs.getObject(colName) == null)
										setString = setString.append(" "
												+ colName + "=null ");
									else
										setString = setString.append(" "
												+ colName + "='"
												+ rs.getObject(colName) + "' ");
								}
							}
							// //end of getting new column data in scripts

							boolean andCond = false;
							for (String colName : pkeycols) {
								if (andCond)
									condString = condString.append(" AND ");
								andCond = true;
								if (rs.getObject(colName) == null)
									condString = condString.append(colName
											+ " IS NULL ");
								else
									condString = condString.append(colName
											+ "='" + rs.getObject(colName)
											+ "'");
							}
							// System.out.println(setString.length()+"   "+condString.length());
							if (setString.length() > 1
									&& condString.length() > 1) {
								String str = "UPDATE " + tsd.ti2.getTbName()
										+ " SET " + setString + " WHERE "
										+ condString + " ;";
								// System.out.println(str);
								updateScripts.add(str);
							}
							checkExist = true;
							break;
						} else {

							for (String colName : pkeycols) {
								// System.out.println("compare : "+rs.getObject(colName).toString()+"\t"+rs1.getObject(colName).toString());
								int compValue = (rs.getObject(colName) + "")
										.compareTo((rs1.getObject(colName) + ""));
								// System.out.println("Comvalue : "+compValue);
								noOfCompare++;

								if (compValue == 0) {

									continue;
								} else if (compValue > 0) {
									min = mid + 1;

									break;
								} else {
									high = mid - 1;
									break;
								}

							}
							// System.out.println("Mid : "+mid
							// +" high "+high+" Min : "+min);
						}

					}

				}

				if (!checkExist) {
					StringBuilder colString = new StringBuilder();
					StringBuilder valString = new StringBuilder();
					boolean comma = false;
					for (String colName : matchingCols) {
						// System.out.println(rs.getObject(colName)+" \t "+rs1.getObject(colName));
						// if(!(rs.getObject(colName)+"").equals(rs1.getObject(colName)+""))

						if (comma) {
							colString = colString.append(",");
							valString = valString.append(",");
						}
						comma = true;
						colString = colString.append(" " + colName + " ");
						if (rs.getObject(colName) == null)
							valString = valString.append("null");
						else
							valString = valString.append("'"
									+ rs.getObject(colName) + "'");

					}

					// //for new column data to be added in scripts . appending
					// new column data
					if (nwColCond.equals("Y")) {
						for (String colName : newColList) {
							// System.out.println(rs.getObject(colName)+" \t "+rs1.getObject(colName));
							if (comma) {
								colString = colString.append(",");
								valString = valString.append(",");
							}
							comma = true;
							colString = colString.append(" " + colName + " ");
							if (rs.getObject(colName) == null)
								valString = valString.append("null");
							else
								valString = valString.append("'"
										+ rs.getObject(colName) + "'");

						}
					}
					// //end of getting new column data in scripts

					String str = "INSERT INTO " + tsd.ti2.getTbName() + "  ("
							+ colString + ") VALUES  (" + valString + ");";
					// System.out.println(str);
					insertScripts.add(str);
				}
			}

		}
		System.out.println(new Date() + "mmmmmmmmmmm");
		// ----------------------------------------------------------------------------------------------------
		// rs.last();

		totalrowsCount = countRows;// rs.getRow();//
		rs.beforeFirst();
		rs1.beforeFirst();
		/*
		 * for(;rs.next();) { System.out.println(totalrowsCount++); }
		 */

		for (; rs1.next();) {

			if (rs.first()) {

				int min = 1;
				int high = totalrowsCount;

				boolean checkExist = false;
				while (min <= high) {
					int mid = (high + min) / 2;

					if (rs.absolute(mid)) {
						int counter = 0;
						for (String colName : pkeycols) {
							noOfCompare++;
							// System.out.println(colName+" : check match : "+rs.getObject(colName).toString()+"\t"+rs1.getObject(colName).toString());
							if ((rs1.getObject(colName) + "").equals((rs
									.getObject(colName) + ""))) {
								// System.out.println("**************"+colName);
								counter++;
							}
						}

						if (counter == pkeycols.length) {
							checkExist = true;
							break;
						} else {

							for (String colName : pkeycols) {
								noOfCompare++;
								// System.out.println("compare : "+rs.getObject(colName).toString()+"\t"+rs1.getObject(colName).toString());
								int compValue = (rs1.getObject(colName) + "")
										.compareTo((rs.getObject(colName) + ""));
								// System.out.println("Comvalue : "+compValue);

								if (compValue == 0) {

									continue;
								} else if (compValue > 0) {
									min = mid + 1;

									break;
								} else {
									high = mid - 1;
									break;
								}

							}
							// System.out.println("Mid : "+mid
							// +" high "+high+" Min : "+min);
						}

					}

				}

				if (!checkExist) {
					StringBuilder condString = new StringBuilder();
					boolean andCond = false;
					for (String colName : pkeycols) {
						if (andCond)
							condString = condString.append(" AND ");
						andCond = true;
						if (rs1.getObject(colName) == null)
							condString = condString.append(colName
									+ " IS NULL ");
						else
							condString = condString.append(colName + "='"
									+ rs1.getObject(colName) + "' ");

					}
					String str = "DELETE FROM " + tsd.ti2.getTbName()
							+ " WHERE " + condString + " ;";
					// System.out.println(str);
					deleteScripts.add(str);

				}
			}
		}
		System.out.println(new Date() + "lllllllllllllll");
		// //////////////////////////////////////////////////////////////////////////

		try {
			PrintWriter pw = new PrintWriter(new FileWriter(new File(
					"/tmp/CompTableInfo.txt"), true), true);
			pw.println(new Date() + " >> Source Table Name : "
					+ tsd.ti1.getTbName() + " >> RowsCount : " + countRows
					+ " >> Target Table Name : " + tsd.ti2.getTbName()
					+ " >> RowsCount : " + countRows1);
			pw.flush();
			pw.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		GenScriptsList gsl = new GenScriptsList(deleteScripts, insertScripts,
				updateScripts);
		gsl.setTableName(tsd.ti2.getTbName());
		System.out.println("No of Compare : " + noOfCompare);
		return gsl;
	}

	public static void genScriptFile(GenScriptsList gsl, String file)
			throws IOException {
		File f = new File(file);

		System.out.println(file + "  >>>>" + f.getAbsolutePath());
		PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(f,
				true)));
		System.out.println("Comparison scripts of " + gsl.getTableName()
				+ " table is being written...");
		out.println("--Start of comparison [" + gsl.getTableName()
				+ "] Table, output is in form of DML Scripts : ");
		out.println("--**********************************************************************************");
		out.println("--Insert scripts --------------------------------------------------------------------");
		for (String script : gsl.getInsertScripts()) {
			out.println(script);
		}
		out.println("--Update scripts --------------------------------------------------------------------");
		for (String script : gsl.getUpdateScripts()) {
			out.println(script);
		}
		out.println("--Delete scripts --------------------------------------------------------------------");
		for (String script : gsl.getDeleteScripts()) {
			out.println(script);
		}
		out.println("--End of comparison of [" + gsl.getTableName()
				+ "] Table----------------------------------");
		out.flush();
		out.close();
		System.out.println("Comparison of [" + gsl.getTableName()
				+ "] Table is done");

	}

	public static void initiateDBComparator(String args[]) {
		try {
			Connection con[] = ConnectionProvider.getConnection();
			String tables = ConnectionProvider.p.getProperty("tablelist");
			String tables1 = ConnectionProvider.p.getProperty("tablelist1");

			String pkey = ConnectionProvider.p.getProperty("pkey");
			String pkey1 = ConnectionProvider.p.getProperty("pkey1");

			String condition = ConnectionProvider.p.getProperty("condition");
			String condition1 = ConnectionProvider.p.getProperty("condition1");
			// System.out.println(tables);
			String tableList[] = tables.split("\\|");
			String tableList1[] = tables1.split("\\|");

			String pkeyList[] = pkey.split("\\|");
			String pkeyList1[] = pkey1.split("\\|");

			String conditionList[] = condition.split("\\|");
			String conditionList1[] = condition1.split("\\|");

			// System.out.println(tableList+"no is "+tableList.length);
			// ////System.out.println(conditionList.length+"length of condition >> "+condition+" >> "+condition1);
			File file = new File("/tmp/" + args[0]);
			if (file.exists())
				file.delete();

			for (int i = 0; i < tableList.length; i++) {
				String query = null;
				String query1 = null;
				String countQuery = null;
				String countQuery1 = null;

				// ////////////////////////////222222222222
				String tableCond = null;
				String currTabName = tableList[i];
				String currTabName1 = tableList1[i];
				if (i >= conditionList.length)
					tableCond = null;
				else
					tableCond = conditionList[i];
				if(tableCond !=null)
				{
					tableCond = tableCond + "";
					tableCond = tableCond.trim();
				}
								
				if ((tableCond == null) || (tableCond.length() == 0)) {
					query = "SELECT * FROM " + currTabName + " ";
					query1 = "SELECT * FROM " + currTabName1 + " ";
					countQuery = "SELECT COUNT(*) FROM " + currTabName + " ";
					countQuery1 = "SELECT COUNT(*) FROM " + currTabName1 + " ";
				} else {
					query = "SELECT /*+ result_cache */ * FROM " + currTabName
							+ " WHERE " + tableCond;
					query1 = "SELECT /*+ result_cache */ * FROM "
							+ currTabName1 + " WHERE " + tableCond;
					countQuery = "SELECT COUNT(*) FROM " + currTabName
							+ " WHERE " + tableCond;
					countQuery1 = "SELECT COUNT(*) FROM " + currTabName1
							+ " WHERE " + tableCond;
				}
				StringBuilder ordList = null;
				StringBuilder ordList1 = null;
				// //////////////////
				if (pkeyList[i].length() > 0) {
					ordList = new StringBuilder(" ORDER BY ");
					ordList.append(pkeyList[i]);
					ordList1 = new StringBuilder(" ORDER BY ");
					ordList1.append(pkeyList1[i]);
					query = query + ordList;
					query1 = query1 + ordList;
				}

				// /////////////////////////
				System.out.println(query + "   " + query1);
				Statement st = con[0].createStatement(
						ResultSet.TYPE_SCROLL_INSENSITIVE,
						ResultSet.CONCUR_READ_ONLY);

				// String query =
				// "select /*+ result_cache */ * from emp where empno < : 1";
				((oracle.jdbc.OracleConnection) con[0])
						.setImplicitCachingEnabled(true);
				((oracle.jdbc.OracleConnection) con[0])
						.setStatementCacheSize(10);

				((oracle.jdbc.OracleConnection) con[1])
						.setImplicitCachingEnabled(true);
				((oracle.jdbc.OracleConnection) con[1])
						.setStatementCacheSize(10);

				ResultSet rs = st.executeQuery(query);
				ResultSetMetaData rsmd = rs.getMetaData();

				Statement st1 = con[1].createStatement(
						ResultSet.TYPE_SCROLL_INSENSITIVE,
						ResultSet.CONCUR_READ_ONLY);
				ResultSet rs1 = st1.executeQuery(query1);
				ResultSetMetaData rsmd1 = rs1.getMetaData();

				Statement countSt = con[0].createStatement();
				Statement countSt1 = con[1].createStatement();

				ResultSet countRs = countSt.executeQuery(countQuery);
				ResultSet countRs1 = countSt1.executeQuery(countQuery1);
				countRs.next();
				countRs1.next();
				long countRows = countRs.getLong(1);
				long countRows1 = countRs1.getLong(1);
				countRs = null;
				countRs1 = null;
				System.out.println(countRows + "    " + countRows1);

				TableStructureDiff tsd = compareTableStructure(
						getTableDetails(rsmd, tableList[i], pkeyList[i]),
						getTableDetails(rsmd1, tableList1[i], pkeyList1[i]));
				// GenScriptsList gsl= compareTableData(rs, rs1, tsd);
				GenScriptsList gsl = compareTableDataEnhance(rs, rs1, tsd,
						(int) countRows, (int) countRows1);

				genScriptFile(gsl, "/tmp/" + args[0]);
				rs.close();
				rs1.close();
				st.close();
				st1.close();
				countSt.close();
				countSt1.close();

			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public static void main(String args[]) {
		initiateDBComparator(args);
		// generateDDLScript(new String []{"ddl.sql"});
		// getPrimaryKeysOfTable(null, null);
	}

	public static void generateDDLScript(String args[]) {

		try {
			Connection con[] = ConnectionProvider.getConnection();
			String tables = ConnectionProvider.p.getProperty("tablelist");
			String tables1 = ConnectionProvider.p.getProperty("tablelist1");

			// System.out.println(tables);
			String tableList[] = tables.split("\\|");
			String tableList1[] = tables1.split("\\|");

			// System.out.println(tableList+"no is "+tableList.length);
			File file = new File("/tmp/" + args[0]);
			if (file.exists())
				file.delete();

			for (int i = 0; i < tableList.length; i++) {
				String query = null;
				String query1 = null;

				// ////////////////////////////222222222222
				String tableCond = null;
				String currTabName = tableList[i];
				String currTabName1 = tableList1[i];

				query = "SELECT * FROM " + currTabName + " WHERE ROWNUM<2";
				query1 = "SELECT * FROM " + currTabName1 + " WHERE ROWNUM<2";

				// /////////////////////////
				System.out.println(query + "   " + query1);
				Statement st = con[0].createStatement(
						ResultSet.TYPE_SCROLL_INSENSITIVE,
						ResultSet.CONCUR_READ_ONLY);

				// String query =
				// "select /*+ result_cache */ * from emp where empno < : 1";
				((oracle.jdbc.OracleConnection) con[0])
						.setImplicitCachingEnabled(true);
				((oracle.jdbc.OracleConnection) con[0])
						.setStatementCacheSize(10);

				((oracle.jdbc.OracleConnection) con[1])
						.setImplicitCachingEnabled(true);
				((oracle.jdbc.OracleConnection) con[1])
						.setStatementCacheSize(10);

				ResultSet rs = st.executeQuery(query);
				ResultSetMetaData rsmd = rs.getMetaData();

				Statement st1 = con[1].createStatement(
						ResultSet.TYPE_SCROLL_INSENSITIVE,
						ResultSet.CONCUR_READ_ONLY);
				ResultSet rs1 = st1.executeQuery(query1);
				ResultSetMetaData rsmd1 = rs1.getMetaData();

				TableStructureDiff tsd = compareTableStructure(
						getTableDetails(rsmd, tableList[i],
								getPrimaryKeysOfTable(con[0], tableList[i])),
						getTableDetails(rsmd1, tableList1[i],
								getPrimaryKeysOfTable(con[1], tableList1[i])));
				ArrayList<DDLScript> colLevelDDL = getDDLScriptColumnLevel(tsd);
				genDDLScriptFile(colLevelDDL, "/tmp/" + args[0]);
				// for(DDLScript ddl: colLevelDDL)
				// System.out.println(ddl);
				// genScriptFile(gsl, "/tmp/"+args[0]);
				rs.close();
				rs1.close();
				st.close();
				st1.close();

			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}

	public static void genDDLScriptFile(ArrayList<DDLScript> colLevelDDL,
			String file) throws IOException {
		File f = new File(file);

		System.out.println(file + "  >>>>" + f.getAbsolutePath());
		PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(f,
				true)));
		if (colLevelDDL.size() > 0) {
			System.out.println("Comparison DDL scripts of "
					+ colLevelDDL.get(0).getTableName()
					+ " table is being written...");
			out.println("--Start of comparison ["
					+ colLevelDDL.get(0).getTableName()
					+ "] Table, output is in form of DDL Scripts : ");
			out.println("--**********************************************************************************");
			out.println("--DDL scripts --------------------------------------------------------------------");
			for (DDLScript script : colLevelDDL) {
				out.println(script);
			}

			out.println("--End of comparison DDL of ["
					+ colLevelDDL.get(0).getTableName()
					+ "] Table----------------------------------");
			out.flush();
			out.close();
			System.out.println("Comparison of ["
					+ colLevelDDL.get(0).getTableName() + "] Table is done");
		}
	}

	public static ArrayList<DDLScript> getDDLScriptColumnLevel(
			TableStructureDiff tsd) {

		return tsd.getColLevelDDLScript();
	}

	public static String getPrimaryKeysOfTable(Connection con,
			String tableNameWithSchema) {
		StringBuffer listPK = new StringBuffer();
		try {

			DatabaseMetaData dbmd = con.getMetaData();
			String tablePart[] = tableNameWithSchema.split("\\.");
			String schema = tablePart[0];
			String tab_name = tablePart[1];
			System.out.println(schema + " >> " + tab_name.toUpperCase());
			ResultSet rsPK = dbmd.getPrimaryKeys(null, schema.toUpperCase(),
					tab_name.toUpperCase());

			while (rsPK.next()) {
				listPK.append(rsPK.getString(4) + ",");
			}
			// listPK.substring(,(listPK.length()-1));

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(listPK);
		return listPK.toString();
	}

}

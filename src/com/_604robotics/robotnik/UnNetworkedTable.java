package com._604robotics.robotnik;

import java.nio.ByteBuffer;
import java.util.Set;

import edu.wpi.first.wpilibj.tables.ITable;
import edu.wpi.first.wpilibj.tables.ITableListener;
import edu.wpi.first.wpilibj.tables.TableKeyNotDefinedException;

public class UnNetworkedTable implements ITable {

	public UnNetworkedTable() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void addSubTableListener(ITableListener arg0) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();

	}

	@Override
	public void addSubTableListener(ITableListener arg0, boolean arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void addTableListener(ITableListener arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void addTableListener(ITableListener arg0, boolean arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void addTableListener(String arg0, ITableListener arg1, boolean arg2) {
		// TODO Auto-generated method stub

	}

	@Override
	public void addTableListenerEx(ITableListener arg0, int arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void addTableListenerEx(String arg0, ITableListener arg1, int arg2) {
		// TODO Auto-generated method stub

	}

	@Override
	public void clearFlags(String arg0, int arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void clearPersistent(String arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean containsKey(String arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean containsSubTable(String arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void delete(String arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean getBoolean(String arg0) throws TableKeyNotDefinedException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean getBoolean(String arg0, boolean arg1) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean[] getBooleanArray(String arg0) throws TableKeyNotDefinedException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean[] getBooleanArray(String arg0, boolean[] arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean[] getBooleanArray(String arg0, Boolean[] arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public double getDouble(String arg0) throws TableKeyNotDefinedException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double getDouble(String arg0, double arg1) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getFlags(String arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getInt(String arg0) throws TableKeyNotDefinedException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getInt(String arg0, int arg1) throws TableKeyNotDefinedException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Set<String> getKeys() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<String> getKeys(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public double getNumber(String arg0) throws TableKeyNotDefinedException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double getNumber(String arg0, double arg1) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double[] getNumberArray(String arg0) throws TableKeyNotDefinedException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public double[] getNumberArray(String arg0, double[] arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Double[] getNumberArray(String arg0, Double[] arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public byte[] getRaw(String arg0) throws TableKeyNotDefinedException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public byte[] getRaw(String arg0, byte[] arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getString(String arg0) throws TableKeyNotDefinedException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getString(String arg0, String arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String[] getStringArray(String arg0) throws TableKeyNotDefinedException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String[] getStringArray(String arg0, String[] arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ITable getSubTable(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<String> getSubTables() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getValue(String arg0) throws TableKeyNotDefinedException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getValue(String arg0, Object arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isPersistent(String arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean putBoolean(String arg0, boolean arg1) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean putBooleanArray(String arg0, boolean[] arg1) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean putBooleanArray(String arg0, Boolean[] arg1) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean putDouble(String arg0, double arg1) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean putInt(String arg0, int arg1) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean putNumber(String arg0, double arg1) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean putNumberArray(String arg0, double[] arg1) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean putNumberArray(String arg0, Double[] arg1) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean putRaw(String arg0, byte[] arg1) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean putRaw(String arg0, ByteBuffer arg1, int arg2) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean putString(String arg0, String arg1) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean putStringArray(String arg0, String[] arg1) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean putValue(String arg0, Object arg1) throws IllegalArgumentException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void removeTableListener(ITableListener arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void retrieveValue(String arg0, Object arg1) throws TableKeyNotDefinedException {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean setDefaultBoolean(String arg0, boolean arg1) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean setDefaultBooleanArray(String arg0, boolean[] arg1) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean setDefaultBooleanArray(String arg0, Boolean[] arg1) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean setDefaultNumber(String arg0, double arg1) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean setDefaultNumberArray(String arg0, double[] arg1) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean setDefaultNumberArray(String arg0, Double[] arg1) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean setDefaultRaw(String arg0, byte[] arg1) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean setDefaultString(String arg0, String arg1) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean setDefaultStringArray(String arg0, String[] arg1) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setFlags(String arg0, int arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setPersistent(String arg0) {
		// TODO Auto-generated method stub

	}

}

/*
 * Copyright (c) 2015 by Rafael Angel Aznar Aparici (rafaaznar at gmail dot com)
 * 
 * openAUSIAS: The stunning micro-library that helps you to develop easily 
 *             AJAX web applications by using Java and jQuery
 * openAUSIAS is distributed under the MIT License (MIT)
 * Sources at https://github.com/rafaelaznar/openAUSIAS
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package net.daw.data.specific.implementation;

import net.daw.data.publicinterface.DataInterface;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import net.daw.helper.statics.ExceptionBooster;
import net.daw.helper.statics.SqlBuilder;

public class MysqlDataSpImpl implements DataInterface {

    Connection connection = null;

    public MysqlDataSpImpl(Connection pooledConnection) {
        connection = pooledConnection;
    }

    public String getTipoProductos(Integer id) throws Exception {
        String strResult = null;
        Statement oStatement = null;
        ResultSet oResultSet = null;
        try {
            oStatement = (Statement) connection.createStatement();
            String strSQL = "SELECT tipoproducto.descripcion from tipoproducto, producto, compra where tipoproducto.id=producto.id_tipoproducto and producto.id=compra.id_producto and compra.id_usuario=" + id;
            oResultSet = oStatement.executeQuery(strSQL);
            strResult = "[";
            while (oResultSet.next()) {
                strResult += oResultSet.getString("descripcion") + ",";
            }
            strResult = strResult.substring(0, strResult.length() - 1);
            strResult += "]";
        } catch (SQLException ex) {
            ExceptionBooster.boost(new Exception(this.getClass().getName() + ":getId ERROR: Can't process query: " + ex.getMessage()));
        } finally {
            if (oResultSet != null) {
                oResultSet.close();
            }
            if (oStatement != null) {
                oStatement.close();
            }
        }
        return strResult;
    }

    @Override
    public int insertOne(String strTabla) throws Exception {
        ResultSet oResultSet = null;
        java.sql.PreparedStatement oPreparedStatement = null;
        int id = 0;
        try {
            String strSQL = "INSERT INTO " + strTabla + " (id) VALUES (null) ";
            oPreparedStatement = connection.prepareStatement(strSQL, Statement.RETURN_GENERATED_KEYS);
            int returnLastInsertId = oPreparedStatement.executeUpdate();
            if (returnLastInsertId != -1) {
                oResultSet = oPreparedStatement.getGeneratedKeys();
                oResultSet.next();
                id = oResultSet.getInt(1);
            } else {
                ExceptionBooster.boost(new Exception(this.getClass().getName() + ":insertOne ERROR inserting register"));
            }
        } catch (SQLException ex) {
            ExceptionBooster.boost(new Exception(this.getClass().getName() + ":insertOne ERROR inserting register: " + ex.getMessage()));
        } finally {
            if (oResultSet != null) {
                oResultSet.close();
            }
            if (oPreparedStatement != null) {
                oPreparedStatement.close();
            }
        }
        return id;
    }

    @Override
    public int updateOne(int intId, String strTabla, String strCampo, String strValor) throws Exception {
        int intResult = 0;
        PreparedStatement oPreparedStatement = null;
        try {
            String strSQL = "UPDATE " + strTabla + " SET " + strCampo + " = '" + strValor + "' WHERE id = ?";
            oPreparedStatement = (PreparedStatement) connection.prepareStatement(strSQL);
            oPreparedStatement.setInt(1, intId);
            intResult = oPreparedStatement.executeUpdate();
        } catch (SQLException ex) {
            ExceptionBooster.boost(new Exception(this.getClass().getName() + ":updateOne ERROR updating register: " + ex.getMessage()));
        } finally {
            if (oPreparedStatement != null) {
                oPreparedStatement.close();
            }
        }
        return intResult;
    }

    @Override
    public int removeOne(int intId, String strTabla) throws Exception {
        PreparedStatement oPreparedStatement = null;
        int intResult = 0;
        try {
            String strSQL = "DELETE FROM " + strTabla + " WHERE id = ?";
            oPreparedStatement = (PreparedStatement) connection.prepareStatement(strSQL);
            oPreparedStatement.setInt(1, intId);
            intResult = oPreparedStatement.executeUpdate();
        } catch (SQLException ex) {
            ExceptionBooster.boost(new Exception(this.getClass().getName() + ":removeOne ERROR removing register: " + ex.getMessage()));
        } finally {
            if (oPreparedStatement != null) {
                oPreparedStatement.close();
            }
        }
        return intResult;
    }

    @Override
    public String getId(String strTabla, String strCampo, String strValor) throws Exception {
        String strResult = null;
        Statement oStatement = null;
        ResultSet oResultSet = null;
        try {
            oStatement = (Statement) connection.createStatement();
            String strSQL = "SELECT id FROM " + strTabla + " WHERE " + strCampo + "='" + strValor + "'";
            oResultSet = oStatement.executeQuery(strSQL);
            if (oResultSet.next()) {
                strResult = oResultSet.getString("id");
            } else {
                ExceptionBooster.boost(new Exception(this.getClass().getName() + ":getId ERROR: ID not exists"));
            }
        } catch (SQLException ex) {
            ExceptionBooster.boost(new Exception(this.getClass().getName() + ":getId ERROR: Can't process query: " + ex.getMessage()));
        } finally {
            if (oResultSet != null) {
                oResultSet.close();
            }
            if (oStatement != null) {
                oStatement.close();
            }
        }
        return strResult;
    }

    @Override
    public String getOne(String strSqlSelectDataOrigin, String strCampo, int id) throws Exception {
        String strResult = null;
        PreparedStatement oPreparedStatement = null;
        ResultSet oResultSet = null;
        String strSQL = "";
        try {
            strSQL = strSqlSelectDataOrigin.substring(0, strSqlSelectDataOrigin.indexOf("1=1") + 3) + " AND id=" + id + " " + strSqlSelectDataOrigin.substring(strSqlSelectDataOrigin.indexOf("1=1") + 3, strSqlSelectDataOrigin.length());
            oPreparedStatement = connection.prepareStatement(strSQL);
            oResultSet = oPreparedStatement.executeQuery();
            if (oResultSet.next()) {
                strResult = oResultSet.getString(strCampo);
            } else {
                ExceptionBooster.boost(new Exception(this.getClass().getName() + ":getOne ERROR: ID not exists: " + id));
            }
        } catch (Exception ex) {
            ExceptionBooster.boost(new Exception(this.getClass().getName() + ":getOne ERROR: Can't process query: " + ex.getMessage()));
        } finally {
            if (oResultSet != null) {
                oResultSet.close();
            }
            if (oPreparedStatement != null) {
                oPreparedStatement.close();
            }
        }
        return strResult;
    }

    @Override
    public int getCount(String strSqlSelectDataOrigin) throws Exception {
        int intResult = 0;
        Statement oStatement = null;
        ResultSet oResultSet = null;
        try {
            oStatement = (Statement) connection.createStatement();
            String strNewSqlDataSource = "SELECT COUNT(*) " + strSqlSelectDataOrigin.substring(strSqlSelectDataOrigin.indexOf("from"), strSqlSelectDataOrigin.length());
            oStatement = (Statement) connection.createStatement();
            oResultSet = oStatement.executeQuery(strNewSqlDataSource);
            while (oResultSet.next()) {
                intResult = oResultSet.getInt("COUNT(*)");
            }
        } catch (SQLException ex) {
            ExceptionBooster.boost(new Exception(this.getClass().getName() + ":getCountSQL ERROR:  Can't process query: " + ex.getMessage()));
        } finally {
            if (oResultSet != null) {
                oResultSet.close();
            }
            if (oStatement != null) {
                oStatement.close();
            }
        }
        return intResult;
    }

    @Override
    public int getPages(String strSqlSelectDataOrigin, int intRegsPerPage) throws Exception {
        int intResult = 0;
        int intCount = 0;
        Statement oStatement = null;
        try {
            intCount = Math.max(this.getCount(strSqlSelectDataOrigin), 1);
            intResult = (intCount - 1) / intRegsPerPage;
            intResult++;
        } catch (SQLException ex) {
            ExceptionBooster.boost(new Exception(this.getClass().getName() + ":getPagesSQL ERROR:  Can't process query: " + ex.getMessage()));
        } finally {

            if (oStatement != null) {
                oStatement.close();
            }
        }
        return intResult;
    }

    @Override
    public ResultSet getAllSql(String strSqlSelectDataOrigin) throws Exception {
        Statement oStatement = null;
        ResultSet oResultSet = null;
        try {
            oStatement = (Statement) connection.createStatement();
            oResultSet = oStatement.executeQuery(strSqlSelectDataOrigin);
        } catch (Exception ex) {
            ExceptionBooster.boost(new Exception(this.getClass().getName() + ":getAllSQL ERROR:  Can't process query: " + ex.getMessage()));
        } finally {
        }
        return oResultSet;
    }

    @Override
    public ResultSet getPage(String strSqlSelectDataOrigin, int intRegsPerPage, int intPagina) throws Exception {
        Statement oStatement = null;
        ResultSet oResultSet = null;
        try {
            int intCount = this.getCount(strSqlSelectDataOrigin);
            strSqlSelectDataOrigin += SqlBuilder.buildSqlLimit(intCount, intRegsPerPage, intPagina);
            oStatement = (Statement) connection.createStatement();
            oResultSet = oStatement.executeQuery(strSqlSelectDataOrigin);
        } catch (Exception ex) {
            ExceptionBooster.boost(new Exception(this.getClass().getName() + ":getAllSQL ERROR:  Can't process query: " + ex.getMessage()));
        } finally {
        }
        return oResultSet;
    }

    @Override
    public Boolean existsOne(String strSqlSelectDataOrigin, int id) throws Exception {
        ResultSet oResultSet = null;
        int intResult = 0;
        Statement oStatement = null;
        try {
            oStatement = (Statement) connection.createStatement();
            String strSQL = "SELECT COUNT(*) " + strSqlSelectDataOrigin.substring(strSqlSelectDataOrigin.indexOf("from"), strSqlSelectDataOrigin.length());
            oResultSet = oStatement.executeQuery(strSQL);
            while (oResultSet.next()) {
                intResult = oResultSet.getInt("COUNT(*)");
            }
        } catch (SQLException ex) {
            ExceptionBooster.boost(new Exception(this.getClass().getName() + ":getCountSQL ERROR:  Can't process query: " + ex.getMessage()));
        } finally {
            if (oResultSet != null) {
                oResultSet.close();
            }
            if (oStatement != null) {
                oStatement.close();
            }
        }
        return intResult > 0;
    }

}

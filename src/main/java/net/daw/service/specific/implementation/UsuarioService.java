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
package net.daw.service.specific.implementation;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Calendar;
import net.daw.service.generic.implementation.TableServiceGenImpl;
import javax.servlet.http.HttpServletRequest;
import net.daw.bean.specific.implementation.UsuarioBean;
import net.daw.connection.implementation.BoneConnectionPoolImpl;
import net.daw.connection.publicinterface.ConnectionInterface;
import net.daw.dao.specific.implementation.UsuarioDao;
import net.daw.helper.statics.ExceptionBooster;
import net.daw.helper.statics.JsonMessage;
import java.util.Date;
import net.daw.bean.specific.implementation.CompraBean;
import net.daw.dao.specific.implementation.CompraDao;

public class UsuarioService extends TableServiceGenImpl {

    public UsuarioService(HttpServletRequest request) {
        super(request);
    }

    public String login() throws SQLException, Exception {

        UsuarioBean oUserBean = (UsuarioBean) oRequest.getSession().getAttribute("userBean");
        String strAnswer = null;
        String strCode = "200";
        Integer month = null;
        if (oUserBean == null) {
            String login = oRequest.getParameter("login");
            String pass = oRequest.getParameter("pass");
            if (!login.equals("") && !pass.equals("")) {
                ConnectionInterface DataConnectionSource = null;
                Connection oConnection = null;
                try {
                    DataConnectionSource = new BoneConnectionPoolImpl();
                    oConnection = DataConnectionSource.newConnection();
                    UsuarioBean oUsuario = new UsuarioBean();
                    oUsuario.setLogin(login);
                    oUsuario.setPassword(pass);
                    UsuarioDao oUsuarioDao = new UsuarioDao(oConnection);
                    oUsuario = oUsuarioDao.getFromLogin(oUsuario);

                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(oUsuario.getFnac());
                    Integer m = calendar.get(Calendar.MONTH);
                    month = m + 1;
//                    if (oUsuario.getId() != 0) {
//                        oRequest.getSession().setAttribute("userBean", oUsuario);
//                        strAnswer = oUsuario.getLogin();
//                    } else {
//                        strCode = "403";
//                        strAnswer = "User or password incorrect";
//                    }
                    String passconcatenado;
                    if (month < 10) {
                        passconcatenado = oUsuario.getPassword() + "0" + month;
                    } else {
                        passconcatenado = oUsuario.getPassword() + month;
                    }
                    if (pass.equals(passconcatenado)) {
                        oRequest.getSession().setAttribute("userBean", oUsuario);
                        strAnswer = "{\"status\":\"OK\"}";
                    }
                } catch (Exception ex) {
                    ExceptionBooster.boost(new Exception(this.getClass().getName() + ":login ERROR " + ex.toString()));
                } finally {
                    if (oConnection != null) {
                        oConnection.close();
                    }
                    if (DataConnectionSource != null) {
                        DataConnectionSource.disposeConnection();
                    }
                }
            }
        } else {
            strAnswer = "{\"status\":\"OK\"}";
        }

        return strAnswer;
    }

    public String logout() {
        oRequest.getSession().invalidate();
        return "{\"status\":\"KO\"}";
    }

    public String check() throws SQLException, Exception {
        UsuarioBean oUserBean = (UsuarioBean) oRequest.getSession().getAttribute("userBean");
        String retorno = "";
        if (oUserBean == null) {
            retorno = "{\"status\":\"KO\"}";
        } else {
            retorno = "{\"status\":\"OK\",";
            retorno += "\"id\":" + oUserBean.getId() + ",";
            retorno += "\"nombrecompleto\":\"" + oUserBean.getNombre() + " " + oUserBean.getApe1() + " " + oUserBean.getApe2() + "\",";

            ConnectionInterface DataConnectionSource = null;
            Connection oConnection = null;
            try {
                DataConnectionSource = new BoneConnectionPoolImpl();
                oConnection = DataConnectionSource.newConnection();

                UsuarioDao oUsuarioDao = new UsuarioDao(oConnection);

                retorno += "\"tipos\":\"" + oUsuarioDao.getTipoProductos(oUserBean.getId()) + "\"";

            } catch (Exception ex) {
                ExceptionBooster.boost(new Exception(this.getClass().getName() + ":login ERROR " + ex.toString()));
            } finally {
                if (oConnection != null) {
                    oConnection.close();
                }
                if (DataConnectionSource != null) {
                    DataConnectionSource.disposeConnection();
                }
            }

            retorno += "}";
        }
        return retorno;
    }

    public String getsessionstatus() {
        String strAnswer = null;
        UsuarioBean oUserBean = (UsuarioBean) oRequest.getSession().getAttribute("userBean");
        if (oUserBean == null) {
            return JsonMessage.getJsonMsg("403", "ERROR: You don't have permission to perform this operation");
        } else {
            return JsonMessage.getJsonMsg("200", oUserBean.getLogin());
        }
    }

    public String change() throws SQLException, Exception {
        UsuarioBean oUserBean = (UsuarioBean) oRequest.getSession().getAttribute("userBean");
        String retorno = "";
        if (oUserBean == null) {
            retorno = "{\"status\":\"KO\"}";
        } else {

            String id_usuario = oRequest.getParameter("id_usuario");

            ConnectionInterface DataConnectionSource = null;
            Connection oConnection = null;
            try {
                DataConnectionSource = new BoneConnectionPoolImpl();
                oConnection = DataConnectionSource.newConnection();
                UsuarioBean oUsuario = new UsuarioBean();
                oUsuario.setId(Integer.parseInt(id_usuario));

                UsuarioDao oUsuarioDao = new UsuarioDao(oConnection);
                oUsuario = oUsuarioDao.get(oUsuario, 2);

                oRequest.getSession().setAttribute("userBean", oUsuario);

                retorno = "{\"status\":\"OK\"}";

            } catch (Exception ex) {
                ExceptionBooster.boost(new Exception(this.getClass().getName() + ":login ERROR " + ex.toString()));
            } finally {
                if (oConnection != null) {
                    oConnection.close();
                }
                if (DataConnectionSource != null) {
                    DataConnectionSource.disposeConnection();
                }
            }

        }
        return retorno;
    }

    public String buy() throws SQLException, Exception {
        UsuarioBean oUserBean = (UsuarioBean) oRequest.getSession().getAttribute("userBean");
        String retorno = "";
        if (oUserBean == null) {
            retorno = "{\"status\":\"KO\"}";
        } else {

            String id_producto = oRequest.getParameter("id_producto");
            String cantidad = oRequest.getParameter("cantidad");

            //Date fec=new Date();
            ConnectionInterface DataConnectionSource = null;
            Connection oConnection = null;
            try {
                DataConnectionSource = new BoneConnectionPoolImpl();
                oConnection = DataConnectionSource.newConnection();
                CompraBean oCompra = new CompraBean();
                oCompra.setId_producto(Integer.parseInt(id_producto));
                oCompra.setId_usuario(oUserBean.getId());
                oCompra.setCantidad(Integer.parseInt(cantidad));

                CompraDao oCompraDao = new CompraDao(oConnection);

                oCompraDao.set(oCompra);

                retorno = "{\"status\":\"OK\"}";

            } catch (Exception ex) {
                ExceptionBooster.boost(new Exception(this.getClass().getName() + ":login ERROR " + ex.toString()));
            } finally {
                if (oConnection != null) {
                    oConnection.close();
                }
                if (DataConnectionSource != null) {
                    DataConnectionSource.disposeConnection();
                }
            }

        }
        return retorno;
    }

    public String rem() throws SQLException, Exception {
            String retorno= "";
            ConnectionInterface DataConnectionSource = null;
            Connection oConnection = null;
        
            DataConnectionSource = new BoneConnectionPoolImpl();
            oConnection = DataConnectionSource.newConnection();
            Integer idCompra = Integer.parseInt(oRequest.getParameter("id_compra"));
            CompraBean oCompraBean = new CompraBean();
            oCompraBean.setId(idCompra);
            CompraDao oCompraDao = new CompraDao(oConnection);
            oCompraBean = oCompraDao.get(oCompraBean, 1); //ahora ya tenemos el objeto lleno
            if (oCompraBean.getId() != 0) {
            oCompraDao.remove(oCompraBean);
            retorno = "{\"status\":\"OKKKK\"}";
        } else {
                retorno = "{\"status\":\"KO\"}";
        }

        return retorno;
    }

//    public int sessionuserlevel() {
//        String strAnswer = null;
//        UsuarioBean oUserBean = (UsuarioBean) oRequest.getSession().getAttribute("userBean");
//        if (oUserBean == null) {
//            return 0;
//        } else {
//            return oUserBean.getId_estado();
//        }
//    }
}

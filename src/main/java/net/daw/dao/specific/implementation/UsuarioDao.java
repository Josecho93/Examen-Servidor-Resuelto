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
package net.daw.dao.specific.implementation;

import net.daw.dao.generic.implementation.TableDaoGenImpl;
import java.sql.Connection;
import net.daw.bean.specific.implementation.UsuarioBean;
import net.daw.helper.statics.AppConfigurationHelper;

public class UsuarioDao extends TableDaoGenImpl<UsuarioBean> {

    public UsuarioDao(Connection pooledConnection) throws Exception {
        super(pooledConnection);

    }
    public String getTipoProductos(Integer id) throws Exception {
        
        return oMysql.getTipoProductos(id);
        
        
    }
    
    public UsuarioBean getFromLogin(UsuarioBean oUsuario) throws Exception {
        try {
            String strId = oMysql.getId("usuario", "login", oUsuario.getLogin());
            if (strId == null) {
                oUsuario.setId(0);
            } else {
                
                
                
                oUsuario.setId(Integer.parseInt(strId));
//                Integer intId = Integer.parseInt(strId);
//                oUsuario.setId(intId);
//                String pass = oUsuario.getPassword();
//                oUsuario.setPassword(oMysql.getOne(strSqlSelectDataOrigin, "password", oUsuario.getId()));
//                if (!pass.equals(oUsuario.getPassword())) {
//                    oUsuario.setId(0);
//                }
                oUsuario = this.get(oUsuario, AppConfigurationHelper.getJsonDepth());
            }
            return oUsuario;
        } catch (Exception e) {
            throw new Exception("UsuarioDao.getFromLogin: Error: " + e.getMessage());
        }
    }

//    @Override
//    public UsuarioBeanGenSpImpl get(UsuarioBeanGenSpImpl oUsuarioBean, Integer expand) throws Exception {
//        if (oUsuarioBean.getId() > 0) {
//            try {
//                if (!oMysql.existsOne(strTableOrigin, oUsuarioBean.getId())) {
//                    oUsuarioBean.setId(0);
//                } else {
//                    expand--;
//                    if (expand > 0) {
//                        oUsuarioBean.setLogin(oMysql.getNewOne(strSqlSelectDataOrigin, "login", oUsuarioBean.getId()));
//                        oUsuarioBean.setPassword(null);
//                        oUsuarioBean.setCiudad(oMysql.getNewOne(strSqlSelectDataOrigin, "ciudad", oUsuarioBean.getId()));
//                        oUsuarioBean.setFirma(oMysql.getNewOne(strSqlSelectDataOrigin, "firma", oUsuarioBean.getId()));
//                        oUsuarioBean.setSkin(oMysql.getNewOne(strSqlSelectDataOrigin, "skin", oUsuarioBean.getId()));
//
//                        oUsuarioBean.setId_tipousuario(Integer.parseInt(oMysql.getOne(strTableOrigin, "id_tipousuario", oUsuarioBean.getId())));
//                        oUsuarioBean.setId_estado(Integer.parseInt(oMysql.getOne(strTableOrigin, "id_estado", oUsuarioBean.getId())));
//
//                        TipousuarioBeanGenSpImpl oTipousuario = new TipousuarioBeanGenSpImpl();
//                        oTipousuario.setId(Integer.parseInt(oMysql.getOne(strTableOrigin, "id_tipousuario", oUsuarioBean.getId())));
//                        TipousuarioDaoGenSpImpl oTipousuarioDAO = new TipousuarioDaoGenSpImpl(oConnection);
//                        oTipousuario = oTipousuarioDAO.get(oTipousuario, AppConfigurationHelper.getJsonDepth());
//                        oUsuarioBean.setObj_tipousuario(oTipousuario);
//
//                        EstadoBeanGenSpImpl oEstado = new EstadoBeanGenSpImpl();
//                        oEstado.setId(Integer.parseInt(oMysql.getOne(strTableOrigin, "id_estado", oUsuarioBean.getId())));
//                        EstadoDaoGenSpImpl oEstadoDAO = new EstadoDaoGenSpImpl(oConnection);
//                        oEstado = oEstadoDAO.get(oEstado, AppConfigurationHelper.getJsonDepth());
//                        oUsuarioBean.setObj_estado(oEstado);
//                    }
//                }
//            } catch (Exception ex) {
//                ExceptionBooster.boost(new Exception(this.getClass().getName() + ":get ERROR: " + ex.getMessage()));
//            }
//        } else {
//            oUsuarioBean.setId(0);
//        }
//        return oUsuarioBean;
//    }
//
//    @Override
//    public UsuarioBeanGenSpImpl set(UsuarioBeanGenSpImpl oUsuarioBean) throws Exception {
//        try {
//            Boolean isNew = false;
//
//            if (oUsuarioBean.getId() == 0) {
//                oUsuarioBean.setId(oMysql.insertOne(strTableOrigin));
//                isNew = true;
//            }
//            oMysql.updateOne(oUsuarioBean.getId(), strTableOrigin, "login", oUsuarioBean.getLogin());
//            oMysql.updateOne(oUsuarioBean.getId(), strTableOrigin, "id_tipousuario", oUsuarioBean.getId_tipousuario().toString());
//            oMysql.updateOne(oUsuarioBean.getId(), strTableOrigin, "id_estado", oUsuarioBean.getId_estado().toString());
//            oMysql.updateOne(oUsuarioBean.getId(), strTableOrigin, "ciudad", oUsuarioBean.getCiudad());
//            oMysql.updateOne(oUsuarioBean.getId(), strTableOrigin, "firma", oUsuarioBean.getFirma());
//            oMysql.updateOne(oUsuarioBean.getId(), strTableOrigin, "skin", oUsuarioBean.getSkin());
//            String prueba = oUsuarioBean.getPassword();
//
//            if (isNew == false) {
//                if (oUsuarioBean.getPassword() == null || oUsuarioBean.getPassword().equals("")) {
//                    oMysql.updateOne(oUsuarioBean.getId(), strTableOrigin, "password", oMysql.getOne(strTableOrigin, "password", oUsuarioBean.getId()));
//                } else {
//                    oMysql.updateOne(oUsuarioBean.getId(), strTableOrigin, "password", oUsuarioBean.getPassword());
//                }
//            } else {
//                oMysql.updateOne(oUsuarioBean.getId(), strTableOrigin, "password", oUsuarioBean.getPassword());
//            }
//        } catch (Exception ex) {
//            ExceptionBooster.boost(new Exception(this.getClass().getName() + ":set ERROR: " + ex.getMessage()));
//        }
//        return oUsuarioBean;
//    }

//    public UsuarioBean type(UsuarioBean oUsuarioBean) throws Exception {
//
//        try {
//            AlumnoDao oAlumnoDao = new AlumnoDao();
//            AlumnoBean oAlumnoBean = oAlumnoDao.getFromId_usuario(oUsuarioBean);
//            oUsuarioBean.setTipoUsuario(Enum.TipoUsuario.Alumno);
//        } catch (Exception e1) {
//            try {
//                EmpresaDao oEmpresaDao = new EmpresaDao();
//                EmpresaBean oEmpresaBean = oEmpresaDao.getFromId_usuario(oUsuarioBean);
//                oUsuarioBean.setTipoUsuario(Enum.TipoUsuario.Empresa);
//            } catch (Exception e2) {
//                try {
//                    ProfesorDao oProfesorDao = new ProfesorDao();
//                    ProfesorBean oProfesorBean = oProfesorDao.getFromId_usuario(oUsuarioBean);
//                    oUsuarioBean.setTipoUsuario(Enum.TipoUsuario.Profesor);
//                } catch (Exception e3) {
//                    throw new Exception("UsuarioDao.type: Error: " + e3.getMessage());
//                }
//            }
//        } finally {
//            oMysql.desconexion();
//        }
//        return oUsuarioBean;
//    }
//    @Override
//    public UsuarioBean get(UsuarioBean oUsuarioBean) throws Exception {
//        if (oUsuarioBean.getId() > 0) {
//            try {
//                oMysql.conexion(enumTipoConexion);
//                if (!oMysql.existsOne("usuario", oUsuarioBean.getId())) {
//                    oUsuarioBean.setId(0);
//                } else {
//                    oUsuarioBean.setLogin(oMysql.getOne("usuario", "login", oUsuarioBean.getId()));
//                    oUsuarioBean.setPassword(oMysql.getOne("usuario", "password", oUsuarioBean.getId()));
//                }
//            } catch (Exception e) {
//                throw new Exception("UsuarioDao.getUsuario: Error: " + e.getMessage());
//            } finally {
//                oMysql.desconexion();
//            }
//        } else {
//            oUsuarioBean.setId(0);
//        }
//        return oUsuarioBean;
//    }
//
//
//    @Override
//    public String getDescription(int id) throws Exception {
//        UsuarioBean oUsuarioBean = new UsuarioBean();
//        oUsuarioBean.setId(id);
//        oUsuarioBean = this.get(oUsuarioBean);
//        String description;
//        if (oUsuarioBean.getLogin().length() > 20) {
//            description = oUsuarioBean.getLogin().substring(0, 19) + "...";
//        } else {
//            description = oUsuarioBean.getLogin();
//        }
//        return description;
//    }
}

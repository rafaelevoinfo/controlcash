/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.com.dreamsoft.dao.interfaces;

import java.text.ParseException;
import java.util.List;

/**
 *
 * @author rafael
 */
public interface BancoDados<E> {
    public long cadastrar(E element);
    public boolean alterar(E element);
    public boolean deletar(Integer id);

    public List<E> buscarTodos() throws Exception;
    public E buscar(Integer id) throws ParseException;
    public E buscar(String nome) throws ParseException;


}

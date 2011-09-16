package br.com.dreamsoft.dao.interfaces;

public interface Contas<E> extends BancoDados<E> {
	public E buscar (Double valor);
}

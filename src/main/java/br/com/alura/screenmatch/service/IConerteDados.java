package br.com.alura.screenmatch.service;

public interface IConerteDados {
    <T> T obterDados(String json, Class<T> classe);
}

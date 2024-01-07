package br.com.flores.screenmetch.service;

import com.fasterxml.jackson.core.JsonProcessingException;

public interface IConverteDados {

   <T> T obterDados(String json, Class<T> classe) throws JsonProcessingException;
}

package br.com.api.bgm.models.enums;

import lombok.Getter;

@Getter
public enum TransacaoType {
    receita("receita"),
	despesa("despesa");

	private String tipo;

	TransacaoType(String tipo) {
		this.tipo = tipo;
	}
}

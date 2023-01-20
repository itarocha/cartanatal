package br.itarocha.cartanatal.core.service;

import br.itarocha.cartanatal.core.model.DadosPessoais;
import br.itarocha.cartanatal.core.model.domain.Cidade;
import br.itarocha.cartanatal.core.model.domain.Mapa;
import br.itarocha.cartanatal.adapter.in.web.controller.mapper.MapaMapper;
import br.itarocha.cartanatal.adapter.in.web.controller.dto.CartaNatalResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CartaNatalService {

	@Autowired
	private MapaMapper mapper;

	@Autowired
	private BuscadorService buscadorService;

	@Autowired
	MapaService mapaBuilder;

	public CartaNatalResponse buildMapa(DadosPessoais dadosPessoais) throws Exception{
		Mapa retorno = null;
		Cidade c = buscadorService.findCidade(dadosPessoais.getCidade(), dadosPessoais.getUf());
		if (c != null){
    		retorno = mapaBuilder.build(dadosPessoais, c);
		} else {
			System.out.println("Nao conseguiu localizar cidade");
		}
		return mapper.toCartaNatal(retorno);
	}
	
}


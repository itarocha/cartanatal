package br.itarocha.cartanatal.core;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import br.itarocha.cartanatal.core.model.domain.Cidade;
import br.itarocha.cartanatal.core.model.mapper.MapaMapper;
import br.itarocha.cartanatal.core.model.presenter.CartaNatal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StarMain {

	@Autowired
	private MapaMapper mapper;

	// ("Itamar","29/06/1972","5.0.0", Caxias, MA
	public CartaNatal buildMapa(String nome, String data, String hora, String cidade, String uf ) throws Exception{
		Mapa retorno = null;
		MapaBuilder construtor = MapaBuilder.getInstance(".");
		Cidade c = MapeadorCidades.getInstance().getCidade(cidade, uf);
		if (c != null){
			/*
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
			Date d = sdf.parse(data + " "+hora);
			Calendar cal = Calendar.getInstance();
			cal.setTime(d);
			*/

			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
			LocalDateTime dateTime = LocalDateTime.parse(data + " " + hora, formatter);


    		retorno = construtor.build(nome, dateTime, cidade, uf);
		} else {
			System.out.println("Nao conseguiu localizar cidade");
		}
		return mapper.toCartaNatal(retorno);
	}
	
}
// Site Teoria da Conspiração - Link de mapa
// http://www.deldebbio.com.br/
// http://www.viraj.com.br/
// http://www.sadhana.com.br/cgi-local/mapas/mapanow.cgi


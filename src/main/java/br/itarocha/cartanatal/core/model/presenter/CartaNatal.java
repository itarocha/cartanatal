package br.itarocha.cartanatal.core.model.presenter;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class CartaNatal {

    @JsonProperty(value = "dados_pessoais", index = 1)
    private DadoPessoal dadosPessoais;

    @JsonProperty(value = "planetas_signos", index = 2)
    private List<PlanetaSigno> planetasSignos;

    @JsonProperty(index = 3)
    private List<Cuspide> cuspides;

    @JsonProperty(index = 4)
    private List<Aspecto> aspectos;

/*
{"dados_pessoais": {"nome": "Itamar Rocha Chaves Junior", "data":"29/06/1972", "hora":"05:00", "deltaT":"42,7855", "julDay":"2441497,833829", "lat":"004.51.32S", "lon":"043.21.22W"},
"planetas_signos":[
{"planeta":"sol", "signo":"ca", "casa": "01",  "grau": "097.39.46", "gg":"07", "mm":"39", "ss":"46"},
{"planeta":"lua", "signo":"aq", "casa": "08",  "grau": "307.27.55", "gg":"07", "mm":"27", "ss":"55"},
{"planeta":"mer", "signo":"le", "casa": "02",  "grau": "120.56.02", "gg":"00", "mm":"56", "ss":"02"},
{"planeta":"ven", "signo":"ge", "casa": "12",  "grau": "080.08.45", "gg":"20", "mm":"08", "ss":"45"},
{"planeta":"mar", "signo":"le", "casa": "02",  "grau": "120.25.06", "gg":"00", "mm":"25", "ss":"06"},
{"planeta":"jup", "signo":"cp", "casa": "07",  "grau": "272.52.35", "gg":"02", "mm":"52", "ss":"35"},
{"planeta":"sat", "signo":"ge", "casa": "12",  "grau": "073.39.17", "gg":"13", "mm":"39", "ss":"17"},
{"planeta":"ura", "signo":"li", "casa": "04",  "grau": "194.13.07", "gg":"14", "mm":"13", "ss":"07"},
{"planeta":"net", "signo":"sg", "casa": "06",  "grau": "243.00.15", "gg":"03", "mm":"00", "ss":"15"},
{"planeta":"plu", "signo":"vi", "casa": "04",  "grau": "179.25.17", "gg":"29", "mm":"25", "ss":"17"},
{"planeta":"asc", "signo":"ge", "casa": "01",  "grau": "082.42.05", "gg":"22", "mm":"42", "ss":"05"},
{"planeta":"mce", "signo":"pe", "casa": "10",  "grau": "353.37.21", "gg":"23", "mm":"37", "ss":"21"}
],
"cuspides":[
{"casa":"01", "signo":"ge", "grau": "082.42.05", "gg":"22", "mm":"42", "ss":"05"},
{"casa":"02", "signo":"ca", "grau": "111.08.02", "gg":"21", "mm":"08", "ss":"02"},
{"casa":"03", "signo":"le", "grau": "141.20.46", "gg":"21", "mm":"20", "ss":"46"},
{"casa":"04", "signo":"vi", "grau": "173.37.21", "gg":"23", "mm":"37", "ss":"21"},
{"casa":"05", "signo":"li", "grau": "205.44.21", "gg":"25", "mm":"44", "ss":"21"},
{"casa":"06", "signo":"es", "grau": "235.21.42", "gg":"25", "mm":"21", "ss":"42"},
{"casa":"07", "signo":"sg", "grau": "262.42.05", "gg":"22", "mm":"42", "ss":"05"},
{"casa":"08", "signo":"cp", "grau": "291.08.02", "gg":"21", "mm":"08", "ss":"02"},
{"casa":"09", "signo":"aq", "grau": "321.20.46", "gg":"21", "mm":"20", "ss":"46"},
{"casa":"10", "signo":"pe", "grau": "353.37.21", "gg":"23", "mm":"37", "ss":"21"},
{"casa":"11", "signo":"ar", "grau": "025.44.21", "gg":"25", "mm":"44", "ss":"21"},
{"casa":"12", "signo":"to", "grau": "055.21.42", "gg":"25", "mm":"21", "ss":"42"}
],
"aspectos":[
{"planeta_origem":"sol", "planeta_destino":"jup", "aspecto":"OP", "x":"0", "y":"5"},
{"planeta_origem":"sol", "planeta_destino":"ura", "aspecto":"QD", "x":"0", "y":"7"},
{"planeta_origem":"sol", "planeta_destino":"plu", "aspecto":"QD", "x":"0", "y":"9"},
{"planeta_origem":"lua", "planeta_destino":"mer", "aspecto":"OP", "x":"1", "y":"2"},
{"planeta_origem":"lua", "planeta_destino":"mar", "aspecto":"OP", "x":"1", "y":"4"},
{"planeta_origem":"lua", "planeta_destino":"sat", "aspecto":"TG", "x":"1", "y":"6"},
{"planeta_origem":"lua", "planeta_destino":"ura", "aspecto":"TG", "x":"1", "y":"7"},
{"planeta_origem":"lua", "planeta_destino":"net", "aspecto":"SX", "x":"1", "y":"8"},
{"planeta_origem":"lua", "planeta_destino":"plu", "aspecto":"TG", "x":"1", "y":"9"},
{"planeta_origem":"mer", "planeta_destino":"mar", "aspecto":"CJ", "x":"2", "y":"4"},
{"planeta_origem":"mer", "planeta_destino":"net", "aspecto":"TG", "x":"2", "y":"8"},
{"planeta_origem":"mer", "planeta_destino":"plu", "aspecto":"SX", "x":"2", "y":"9"},
{"planeta_origem":"ven", "planeta_destino":"sat", "aspecto":"CJ", "x":"3", "y":"6"},
{"planeta_origem":"ven", "planeta_destino":"ura", "aspecto":"TG", "x":"3", "y":"7"},
{"planeta_origem":"ven", "planeta_destino":"asc", "aspecto":"CJ", "x":"3", "y":"10"},
{"planeta_origem":"mar", "planeta_destino":"net", "aspecto":"TG", "x":"4", "y":"8"},
{"planeta_origem":"mar", "planeta_destino":"plu", "aspecto":"SX", "x":"4", "y":"9"},
{"planeta_origem":"jup", "planeta_destino":"plu", "aspecto":"QD", "x":"5", "y":"9"},
{"planeta_origem":"sat", "planeta_destino":"ura", "aspecto":"TG", "x":"6", "y":"7"},
{"planeta_origem":"ura", "planeta_destino":"asc", "aspecto":"TG", "x":"7", "y":"10"},
{"planeta_origem":"net", "planeta_destino":"plu", "aspecto":"SX", "x":"8", "y":"9"},
{"planeta_origem":"plu", "planeta_destino":"asc", "aspecto":"QD", "x":"9", "y":"10"},
{"planeta_origem":"plu", "planeta_destino":"mce", "aspecto":"OP", "x":"9", "y":"11"}
]}

 */
}

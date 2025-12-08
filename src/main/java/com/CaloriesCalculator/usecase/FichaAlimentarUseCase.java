package com.CaloriesCalculator.usecase;

import com.CaloriesCalculator.database.entity.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface FichaAlimentarUseCase {

    void salvarProdutoFichaAlimentar(List<String> produtos, int refeicao);

    TipoRefeicaoEnum convertRefeicao(int refeicao);

    List<FichaAlimentarEntity> buscarFichaAlimentar(Long usuarioId);

    Optional<FichaAlimentarEntity> buscarFichaAlimentarDoDia(Long usuarioId, LocalDate data);

    FichaAlimentarEntity buscarFichaAlimentarMaisRecente(Long usuarioId);

    List<RefeicaoEntity> buscarRefeicoesDoDia(Long usuarioId, LocalDate data);

    List<Refeicao_ProdutoAlimenticioEntity> buscarProdutos_Refeicao(Long refeicaoId);

    String retirarProdutoRefeicao(Long id, int refeicaoRecebida);

    void adicionarRefeicaoProdutoSession(int refeicaoId, Long produtoId);

}

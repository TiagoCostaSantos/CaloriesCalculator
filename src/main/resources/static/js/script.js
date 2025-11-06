// Executa só depois que o HTML carregar
document.addEventListener("DOMContentLoaded", function() {
    const params = new URLSearchParams(window.location.search);
    const cadastro = params.get("cadastro");
    if (cadastro === "sucesso") {
        // alerta após 300ms
        setTimeout(() => {
            alert("Produto cadastrado com sucesso!");
        }, 600);
    }
});

// Quando o documento carregar, adiciona o evento ao botão
document.addEventListener("DOMContentLoaded", function() {
  const headers = document.querySelectorAll(".accordion-header");
  headers.forEach(header => {
    header.addEventListener("click", () => {
      const item = header.parentElement;
      item.classList.toggle("active");
    });
  });
});

// CONTEUDO DO MODAL (BUSCAR PRODUTOS)

let modalProdutos;
let refeicaoAtual;

async function abrirModalProdutos(refeicao) {
  try {
    if(refeicaoAtual == null){
        refeicaoAtual = refeicao
    }
     // guarda a refeição atual
    const produto = document.getElementById('inputProduto').value;

    const resp = await fetch(`/produto-alimenticio/buscar-produto?produtoAlimenticio=${encodeURIComponent(produto)}&refeicao=${refeicaoAtual}`);
    //zerar valor do input para as proximas vezes que abrir o modal aparecer todos os produtos
    document.getElementById('inputProduto').value = '';
    const html = await resp.text();
    document.getElementById('conteudoModalProdutos').innerHTML = html;

    // Cria ou recupera a instância corretamente
    if (!modalProdutos) {
      const modalElement = document.getElementById('staticBackdrop');
      modalProdutos = bootstrap.Modal.getOrCreateInstance(modalElement);
    }

    modalProdutos.show();
    restaurarSelecao();

  } catch (err) {
    console.error(err);
    // document.getElementById('conteudoModalProdutos').innerHTML =
    //   '<p class="text-danger">Erro ao carregar produtos.</p>';
  }
}
// TODO TODA VEZ QUE FECHAR O MODAL, LIMPAR PESQUISA[
// TODO TODA VEZ QUE FECHAR O MODAL, LIMPAR CHECKBOX´S SELECIONADOS
// TODO TODA VEZ QUE FECHAR O MODAL LIMPAR REFEICAO(1,2,3,4,5,6)
// limpar pesquisa do modal

//const modalElement = document.getElementById('staticBackdrop');
//
//modalElement.addEventListener('hidden.bs.modal', function () {
//// Quando o modal é fechado, limpar o campo de busca
//document.getElementById('inputProduto').value = '';

// 2 PARA FAZER PESQUISA E CONTINUAR COM OS CHECKBOX´S ATIVOS NO MODAL

let produtosSelecionados = new Set();

//  2.1 Quando marcar/desmarcar
document.addEventListener("change", (e) => {
    if (e.target.classList.contains("produto-checkbox-modal")) {
        const valor = e.target.value;
        if (e.target.checked) {
            produtosSelecionados.add(valor);
        } else {
            produtosSelecionados.delete(valor);
        }
        console.log("Selecionados:", Array.from(produtosSelecionados));
    }
});

// 2.2 Após atualizar o fragmento com a nova busca
function restaurarSelecao() {
    document.querySelectorAll(".produto-checkbox-modal").forEach(chk => {
        if (produtosSelecionados.has(chk.value)) {
            chk.checked = true;
        }
    });
}
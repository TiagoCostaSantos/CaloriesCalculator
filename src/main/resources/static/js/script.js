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

async function abrirModalProdutos() {
  try {

    const produto = document.getElementById('inputProduto').value;

    const resp = await fetch(`/produto-alimenticio/buscar-produto?produtoAlimenticio=${encodeURIComponent(produto)}`);
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

  } catch (err) {
    console.error(err);
    // document.getElementById('conteudoModalProdutos').innerHTML =
    //   '<p class="text-danger">Erro ao carregar produtos.</p>';
  }
}

// limpar pesquisa do modal

//const modalElement = document.getElementById('staticBackdrop');
//
//modalElement.addEventListener('hidden.bs.modal', function () {
//// Quando o modal é fechado, limpar o campo de busca
//document.getElementById('inputProduto').value = '';
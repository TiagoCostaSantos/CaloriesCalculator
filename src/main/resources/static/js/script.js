// Executa só depois que o HTML carregar, quando o produto é cadastrado com sucesso
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

// Adiciona o evento ao botão do modal
document.addEventListener("DOMContentLoaded", function() {
  const headers = document.querySelectorAll(".accordion-header");
  headers.forEach(header => {
    header.addEventListener("click", () => {
      const item = header.parentElement;
      item.classList.toggle("active");
    });
  });
});

// MODAL (DADOS USUARIO)
async function abrirModalDados(){ // ESSSA FUNÇÃO NAÕ TA FAZENDO OS OUTROS BOTTÕES ABRIR
    try{
        const resp2 = await fetch(`/usuario/formSalvarDados`);
        const html2 = await resp2.text();
        document.getElementById('conteudoModalDados').innerHTML = html2;

        // ✅ abre o modal após carregar o conteúdo
        const modal = new bootstrap.Modal(document.getElementById('staticBackdropDados'));
        modal.show();

    } catch (err) {
        console.error(err);
        document.getElementById('conteudoModalDados').innerHTML =
        '<p class="text-danger">Erro ao carregar pagina de dados, por favor tente novamente mais tarde.</p>';
    }
}

async function fecharModalDados() {
    const modalEl = document.getElementById('staticBackdropDados');
    let modal = bootstrap.Modal.getInstance(modalEl) || new bootstrap.Modal(modalEl);
    modal.hide();
}
// CONTEUDO DO MODAL (BUSCAR PRODUTOS)

let modalProdutos;
let refeicaoAtual;
let produtosSelecionados = new Set();

async function abrirModalProdutos(refeicao) {
  try {
    if(refeicaoAtual == null){
        refeicaoAtual = refeicao
    }
     // guarda a refeição atual
    const produto = document.getElementById('inputProduto').value;
    const resp = await fetch(`/produto-alimenticio/buscar-produto?produtoAlimenticio=${encodeURIComponent(produto)}&refeicao=${refeicaoAtual}`);
    const html = await resp.text();
    document.getElementById('conteudoModalProdutos').innerHTML = html;
    //zerar valor do input para as proximas vezes que abrir o modal aparecer todos os produtos
    document.getElementById('inputProduto').value = '';

    // Cria ou recupera a instância corretamente
    if (!modalProdutos) {
      const modalElement = document.getElementById('staticBackdrop');
      modalProdutos = bootstrap.Modal.getOrCreateInstance(modalElement);
    }

    modalProdutos.show();
    restaurarSelecao();

    // Garante que o hidden sempre tenha os dados antes do envio
    document.getElementById("formEnviar").addEventListener("submit", function(e) {
      const hidden = document.getElementById("produtosSelecionados");
      if (hidden) {
        hidden.value = Array.from(produtosSelecionados).join(",");
      }
    });

  } catch (err) {
    console.error(err);
     document.getElementById('conteudoModalProdutos').innerHTML =
       '<p class="text-danger">Erro ao carregar produtos.</p>';
  }
}

// 2 PARA FAZER PESQUISA E CONTINUAR COM OS CHECKBOX´S ATIVOS NO MODAL
// Quando marcar/desmarcar um checkbox
document.addEventListener("change", (e) => {
  if (e.target.classList.contains("produto-checkbox-modal")) {
    const valor = e.target.value;
    if (e.target.checked) {
      produtosSelecionados.add(valor);
    } else {
      produtosSelecionados.delete(valor);
    }

    // Atualiza o hidden dinamicamente
    const hidden = document.getElementById("produtosSelecionados");
    if (hidden) {
      hidden.value = Array.from(produtosSelecionados).join(",");
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

// FECHAR MODAL PRODUTOS LIMPAR SELEÇÕES, BUSCA, E REFEICAO
document.addEventListener('DOMContentLoaded', () => {
  const modalElement = document.getElementById('staticBackdrop');
  // Esse evento é disparado sempre que o modal é FECHADO
  modalElement.addEventListener('hidden.bs.modal', () => {
    // 1️⃣ Limpa o campo de pesquisa
    const input = document.getElementById('inputProduto');
    if (input) input.value = '';
    // 2️⃣ Desmarca todos os checkboxes que estavam marcados
    document.querySelectorAll(".produto-checkbox-modal").forEach(chk => chk.checked = false);
    // 3️⃣ Zera a lista de produtos selecionados
        produtosSelecionados.clear();
    // 4️⃣ Zera refeição atual
    refeicaoAtual = null;
    // 5️⃣ (opcional) limpa o conteúdo do modal
    document.getElementById('conteudoModalProdutos').innerHTML = '';
  });
});

// fechar modal Dados e voltar scroll
document.addEventListener('hidden.bs.modal', () => {
    document.body.classList.remove('modal-open');
    document.body.style.overflow = "auto";
    document.querySelectorAll('.modal-backdrop').forEach(e => e.remove());
});

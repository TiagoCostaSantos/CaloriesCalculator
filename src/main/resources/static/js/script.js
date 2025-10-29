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

Aqui está o README traduzido para o projeto com as classes fornecidas:

# Projeto de Processamento de Imagens

Este projeto inclui classes Java para processamento de imagens e aplicação de filtros. As principais classes incluídas no projeto são:

## Classe `Filter`

A classe `Filter` é responsável pelo carregamento e manipulação de filtros usados no processamento de imagens. Ela possui os seguintes atributos e métodos:

- `filter`: Uma matriz para armazenar o filtro.
- `stride`: A quantidade de deslocamento ao aplicar o filtro.
- `m`: A largura do filtro.
- `n`: A altura do filtro.

### Método `Scanner`

Este método recebe o nome de um arquivo como entrada e lê o conteúdo desse arquivo. Ele é usado para carregar os filtros de arquivos.

### Método `loadFilter`

Este método carrega um filtro de um arquivo específico e preenche a matriz `filter` com os valores do filtro.

### Método `correlationFilter`

Este método aplica o filtro à imagem atual usando a operação de correlação.

### Método `applyBoxFilter`

Este método aplica um filtro de caixa à imagem atual com largura, altura e deslocamento especificados.

### Método `applySobelFilterWithHistogramExpansion`

Este método aplica um filtro Sobel à imagem atual e expande seu histograma.

## Classe `ImageManager`

A classe `ImageManager` lida com o carregamento, conversões e aplicação de filtros em imagens. Ela possui os seguintes atributos e métodos:

- `image`: A imagem atual.
- `originalImage`: A imagem original antes das transformações.
- `selectedImagePath`: O caminho do arquivo da imagem selecionada.
- `hsbValues`: Matriz para armazenar os valores HSB da imagem.
- `filter`: Uma instância da classe `Filter` para aplicação de filtros.

### Método `convertRGBToHSB`

Este método converte a imagem RGB atual para o espaço de cores HSB.

### Método `convertHSBToRGB`

Este método converte a imagem HSB de volta para o espaço de cores RGB.

### Método `changeHue`

Este método altera o matiz da imagem HSB atual.

### Método `changeSaturation`

Este método altera a saturação da imagem HSB atual.

### Método `applyNegativeRGB`

Este método aplica uma transformação negativa à imagem no espaço de cores RGB.

### Método `applyNegativeHSVBandV`

Este método aplica uma transformação negativa à banda de luminância (V) da imagem no espaço de cores HSV.

### Método `loadFilterFromFile`

Este método carrega um filtro de um arquivo e o associa à instância da classe `Filter`.

### Método `correlationFilter`

Este método aplica um filtro à imagem atual usando a operação de correlação.

### Método `applyBoxFilter`

Este método aplica um filtro de caixa à imagem atual com largura, altura e deslocamento especificados.

### Método `applySobelFilterWithHistogramExpansion`

Este método aplica um filtro Sobel à imagem atual e expande seu histograma.

### Método `compareBoxFilters`

Este método compara o desempenho de dois filtros de caixa diferentes na imagem atual.

### Método `revertToOriginalImage`

Este método restaura a imagem para a imagem original carregada.

### Método `displayImage`

Este método exibe a imagem atual em uma janela gráfica redimensionada.

## Classe `Menu`

A classe `Menu` implementa um menu interativo para interagir com as funcionalidades do processamento de imagens. Ele oferece opções para abrir e salvar imagens, aplicar conversões de cores, filtros e muito mais.
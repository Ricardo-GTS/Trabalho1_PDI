package src;

import java.io.IOException;
import java.util.Scanner;

public class Menu {

  private static boolean uploadedImage = false;

  public static void main(String[] args) {
    Scanner scanner = new Scanner(System.in);

    ImageManager imageManager = new ImageManager();
    while (true) {
      System.out.println("\n");
      System.out.println("Menu:");
      System.out.println("A - Abrir imagem");
      System.out.println("S - Salvar imagem");
      System.out.println("E - Exibir imagem");
      System.out.println("1 - Conversões RGB-HSB-RGB");
      System.out.println(
          "2 - Alteração de matiz e saturação no HSB, com posterior conversão a RGB");
      System.out.println("3 - Negativo");
      System.out.println("4 - Filtros");
      System.out.println("5 - Imagem original");
      System.out.println("0 - Sair");

      System.out.print("Escolha uma opção: ");
      String escolha = scanner.next().toUpperCase();

      switch (escolha) {
        case "A" -> {
          System.out.println("Você escolheu Abrir imagem.");
          try {
            imageManager.OpenImage();
          } catch (IOException e) {
            e.printStackTrace();
          }
          if (imageManager.getImage() != null) {
            uploadedImage = true; // Define a variável como true após abrir a imagem
          }
        }
        case "S" -> {
          if (uploadedImage) {
            System.out.println("Você escolheu Salvar imagem.");
            // Implemente a lógica para salvar uma imagem aqui
            try {
              imageManager.saveImage();
            } catch (IOException e) {
              e.printStackTrace();
            }
          } else {
            System.out.println("Não é possível salvar sem uma imagem carregada.");
          }
        }
        case "E" -> {
          if (uploadedImage) {
            System.out.println("Você escolheu Exibir imagem.");
            // Implemente a lógica para exibir uma imagem aqui
            imageManager.displayImage("Menu Principal");
          } else {
            System.out.println("Não é possível exibir sem uma imagem carregada.");
          }
        }
        case "1" -> {
          if (uploadedImage) {
            submenuConversoesRGBHSB(imageManager);
          } else {
            System.out.println(
                "Não é possível realizar essa operação sem uma imagem carregada.");
          }
        }
        case "2" -> {
          if (uploadedImage) {
            submenuAlteracaoMatizSaturacaoHSB(imageManager);
          } else {
            System.out.println(
                "Não é possível realizar essa operação sem uma imagem carregada.");
          }
        }
        case "3" -> {
          if (uploadedImage) {
            submenuNegativo(imageManager);
          } else {
            System.out.println(
                "Não é possível realizar essa operação sem uma imagem carregada.");
          }
        }
        case "4" -> {
          if (uploadedImage) {
            submenuCorrelacaoMNStride(imageManager);
          } else {
            System.out.println(
                "Não é possível realizar essa operação sem uma imagem carregada.");
          }
        }
        case "5" -> {
          if (uploadedImage) {
            System.out.println("Você escolheu Retornar a Imagem original.");
            imageManager.revertToOriginalImage();
          } else {
            System.out.println(
                "Não é possível exibir a imagem original sem uma imagem carregada.");
          }
        }
        case "0" -> {
          System.out.println("Saindo do programa. Adeus!");
          System.exit(0);
        }
        default -> System.out.println("Opção inválida. Por favor, escolha uma opção válida.");
      }
    }
  }

  public static void submenuConversoesRGBHSB(ImageManager imagem) {
    Scanner scanner = new Scanner(System.in);
    while (true) {
      System.out.println("\n");
      System.out.println("Submenu - Conversões RGB-HSB-RGB:");
      System.out.println("1 - RGB->HSB");
      System.out.println("2 - HSB->RGB");
      System.out.println("3 - Exibir imagem");
      System.out.println("4 - Retornar a imagem original");
      System.out.println("0 - Voltar ao menu principal");

      System.out.print("Escolha uma opção: ");
      String subOpcao = scanner.next();

      switch (subOpcao) {
        case "1" -> {
          System.out.println("Você escolheu RGB->HSB.");
          imagem.convertRGBToHSB();
          imagem.displayImage("RGB->HSB");
        }
        case "2" -> {
          System.out.println("Você escolheu HSB->RGB.");
          imagem.convertHSBToRGB();
          imagem.displayImage("HSB->RGB");
        }
        case "3" -> {
          System.out.println("Exibindo imagem.");
          imagem.displayImage("Menu de Conversões");
        }
        case "4" -> {
          System.out.println("Voltando a imagem original.");
          imagem.revertToOriginalImage();
        }
        case "0" -> {
          return;
        }
        default -> System.out.println("Opção inválida. Por favor, escolha uma opção válida.");
      }
    }
  }

  public static void submenuAlteracaoMatizSaturacaoHSB(ImageManager imagem) {
    Scanner scanner = new Scanner(System.in);
    while (true) {
      System.out.println("\n");
      System.out.println("Submenu - Alteração de matiz e saturação no HSB:");
      System.out.println("1 - Alteração de matiz");
      System.out.println("2 - Saturação");
      System.out.println("3 - Exibir imagem");
      System.out.println("4 - Retornar a imagem original");
      System.out.println("0 - Voltar ao menu principal");

      System.out.print("Escolha uma opção: ");
      String subOpcao = scanner.next();

      switch (subOpcao) {
        case "1" -> {
          System.out.print("Digite o valor de alteração de matiz: ");
          double valorMatiz = scanner.nextDouble();
          imagem.changeHue(valorMatiz);
          imagem.displayImage("Alteração de matiz");
        }
        case "2" -> {
          System.out.print("Digite o valor de saturação entre -1 e 1 com virgula: ");
          double valorSaturacao = scanner.nextDouble();
          imagem.changeSaturation(valorSaturacao);
          imagem.displayImage("Alteração Saturação");
        }
        case "3" -> {
          System.out.println("Exibindo imagem.");
          imagem.displayImage("Menu de Alteração de matiz e saturação");
        }
        case "4" -> {
          System.out.println("Voltando a imagem original.");
          imagem.revertToOriginalImage();
        }
        case "0" -> {
          return;
        }
        default -> System.out.println("Opção inválida. Por favor, escolha uma opção válida.");
      }
    }
  }

  public static void submenuNegativo(ImageManager imagem) {
    Scanner scanner = new Scanner(System.in);
    while (true) {
      System.out.println("\n");
      System.out.println("Submenu - Negativo:");
      System.out.println("1 - RGB (banda a banda)");
      System.out.println("2 - Banda V do HSV");
      System.out.println("3 - Exibir imagem");
      System.out.println("4 - Retornar a imagem original");
      System.out.println("0 - Voltar ao menu principal");

      System.out.print("Escolha uma opção: ");
      String subOpcao = scanner.next();

      switch (subOpcao) {
        case "1" -> {
          System.out.println("Você escolheu Negativo RGB (banda a banda).");
          imagem.applyNegativeRGB();
          imagem.displayImage("Negativo RGB");
        }
        case "2" -> {
          System.out.println("Você escolheu Negativo Banda V do HSV.");
          imagem.applyNegativeHSVBandV();
          imagem.displayImage("Negativo Banda V do HSV");
        }
        case "3" -> {
          System.out.println("Exibindo imagem.");
          imagem.displayImage("Menu de Negativo");
        }
        case "4" -> {
          System.out.println("Voltando a imagem original.");
          imagem.revertToOriginalImage();
        }
        case "0" -> {
          return;
        }
        default -> System.out.println("Opção inválida. Por favor, escolha uma opção válida.");
      }
    }
  }

  public static void submenuCorrelacaoMNStride(ImageManager imagem) {
    Scanner scanner = new Scanner(System.in);
    while (true) {
      System.out.println("\n");
      System.out.println("Submenu - De Filtros:");
      System.out.println("1 - Filtro Correlação Com arquivo de Filtro MxN e Stride");
      System.out.println("2 - Filtro Box");
      System.out.println("3 - Filtro Sobel com expansão de histograma");
      System.out.println("4 - Comparar Box15x1(Box1x15(imagem)) com Box15x15(imagem)");
      System.out.println("5 - Exibir imagem");
      System.out.println("6 - Retornar a imagem original");
      System.out.println("0 - Voltar ao menu principal");

      System.out.print("Escolha uma opção: ");
      String subOpcao = scanner.next();

      switch (subOpcao) {
        case "1" -> {
          System.out.println("Você escolheu Correlação m x n com filtro Box.");
          try {
            imagem.loadFilterFromFile("C:\\Users\\luizd\\IdeaProjects\\Trabalho1_PDI\\filter.txt");
            imagem.correlationFilter();
            imagem.displayImage("Correlação filtro com arquivo");
          } catch (IOException e) {
            e.printStackTrace();
          }
        }
        case "2" -> {
          System.out.println("Você escolheu filtro Box.");
          System.out.print("Digite o valor de M :");
          int m = scanner.nextInt();
          System.out.print("Digite o valor de N :");
          int n = scanner.nextInt();
          System.out.print("Digite o valor Do Stride :");
          int stride = scanner.nextInt();
          imagem.applyBoxFilter(m, n, stride);
          imagem.displayImage("Filtro Box" + m + "x" + n + " com Stride " + stride);
        }
        case "3" -> {
          System.out.println("Você escolheu filtro Sobel. com expansão de histograma");
          imagem.applySobelFilterWithHistogramExpansion();
          imagem.displayImage("Filtro Sobel com expansão de histograma");
        }
        case "4" -> {
          System.out.println("Comparando Box15x1(Box1x15(imagem)) com Box15x15(imagem).");
          imagem.compareBoxFilters();
        }
        case "5" -> {
          System.out.println("Exibindo imagem.");
          imagem.displayImage("Menu de Filtros");
        }
        case "6" -> {
          System.out.println("Voltando a imagem original.");
          imagem.revertToOriginalImage();
        }
        case "0" -> {
          return;
        }
        default -> System.out.println("Opção inválida. Por favor, escolha uma opção válida.");
      }
    }
  }
}

package src;
import java.io.IOException;
import java.util.Scanner;

public class Menu {

    private static boolean imagemCarregada = false;

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
                System.out.println("2 - Alteração de matiz e saturação no HSB, com posterior conversão a RGB");
                System.out.println("3 - Negativo");
                System.out.println("4 - Filtros");
                System.out.println("5 - Imagem original");
                System.out.println("0 - Sair");

                System.out.print("Escolha uma opção: ");
                String escolha = scanner.next().toUpperCase();

                switch (escolha) {
                    case "A":
                        System.out.println("Você escolheu Abrir imagem.");
                        // Implemente a lógica para abrir uma imagem aqui
                        try {
                            imageManager.OpenImage();
                            } catch (IOException e) {
                            e.printStackTrace();
                            }
                        if (imageManager.getImage() != null)    
                        imagemCarregada = true; // Define a variável como true após abrir a imagem
                        break;
                    case "S":
                        if (imagemCarregada) {
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
                        break;
                    case "E":
                        if (imagemCarregada) {
                            System.out.println("Você escolheu Exibir imagem.");
                            // Implemente a lógica para exibir uma imagem aqui
                            imageManager.displayImage();
                        } else {
                            System.out.println("Não é possível exibir sem uma imagem carregada.");
                        }
                        break;
                    case "1":
                        if (imagemCarregada) {
                            submenuConversoesRGBHSB(imageManager);
                        } else {
                            System.out.println("Não é possível realizar essa operação sem uma imagem carregada.");
                        }
                        break;
                    case "2":
                        if (imagemCarregada) {
                            submenuAlteracaoMatizSaturacaoHSB(imageManager);
                        } else {
                            System.out.println("Não é possível realizar essa operação sem uma imagem carregada.");
                        }
                        break;
                    case "3":
                        if (imagemCarregada) {
                            submenuNegativo(imageManager);
                        } else {
                            System.out.println("Não é possível realizar essa operação sem uma imagem carregada.");
                        }
                        break;
                    case "4":
                        if (imagemCarregada) {
                            submenuCorrelacaoMNStride(imageManager);
                        } else {
                            System.out.println("Não é possível realizar essa operação sem uma imagem carregada.");
                        }
                        break;
                    case "5":
                        if (imagemCarregada) {
                            System.out.println("Você escolheu Retornar a Imagem original.");
                            imageManager.revertToOriginalImage();
                        } else {
                            System.out.println("Não é possível exibir a imagem original sem uma imagem carregada.");
                        }
                        break;
                    case "0":
                        System.out.println("Saindo do programa. Adeus!");
                        System.exit(0);
                        break;
                    default:
                        System.out.println("Opção inválida. Por favor, escolha uma opção válida.");
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
                    case "1":
                        System.out.println("Você escolheu RGB->HSB.");
                        imagem.convertRGBToHSB();
                        break;
                    case "2":
                        System.out.println("Você escolheu HSB->RGB.");
                        imagem.convertHSBToRGB();
                        break;
                    case "3":
                        System.out.println("Exibindo imagem.");
                        imagem.displayImage();
                        break;
                    case "4":
                        System.out.println("Voltando a imagem original.");
                        imagem.revertToOriginalImage();
                        break;    
                    case "0":
                        return;
                    default:
                        System.out.println("Opção inválida. Por favor, escolha uma opção válida.");
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
                    case "1":
                        System.out.print("Digite o valor de alteração de matiz: ");
                        double valorMatiz = scanner.nextDouble();
                        imagem.changeHue(valorMatiz);    
                        break;
                    case "2":
                        System.out.print("Digite o valor de saturação entre 0 e 1 com virgula: ");
                        double valorSaturacao = scanner.nextDouble();
                        imagem.changeSaturation(valorSaturacao);
                        break;
                    case "3":
                        System.out.println("Exibindo imagem.");
                        imagem.displayImage();
                        break;
                    case "4":
                        System.out.println("Voltando a imagem original.");
                        imagem.revertToOriginalImage();
                        break;        
                    case "0":
                        return;
                    default:
                        System.out.println("Opção inválida. Por favor, escolha uma opção válida.");
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
                    case "1":
                        System.out.println("Você escolheu Negativo RGB (banda a banda).");
                        imagem.applyNegativeRGB();
                        break;
                    case "2":
                        System.out.println("Você escolheu Negativo Banda V do HSV.");
                        imagem.applyNegativeHSVBandV();
                        break;
                    case "3":
                        System.out.println("Exibindo imagem.");
                        imagem.displayImage();
                        break;
                    case "4":
                        System.out.println("Voltando a imagem original.");
                        imagem.revertToOriginalImage();
                        break;        
                    case "0":
                        return;
                    default:
                        System.out.println("Opção inválida. Por favor, escolha uma opção válida.");
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
                    case "1":
                        System.out.println("Você escolheu Correlação m x n com filtro Box.");
                        try {
                            imagem.loadFilterFromFile("filter.txt");
                            imagem.correlationFilter();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        break;
                    case "2":
                        System.out.println("Você escolheu filtro Box.");
                        System.out.print("Digite o valor de M :");
                        int M = scanner.nextInt();
                        System.out.print("Digite o valor de N :");
                        int N = scanner.nextInt();
                        System.out.print("Digite o valor Do Stride :");
                        int Stride = scanner.nextInt();
                        imagem.applyBoxFilter(M, N, Stride);
                        break;    
                    case "3":
                        System.out.println("Você escolheu filtro Sobel. com expansão de histograma");
                        // Implemente a lógica para a correlação com filtro Sobel aqui
                        break;
                    case "4":
                        System.out.println("Comparando Box15x1(Box1x15(imagem)) com Box15x15(imagem).");
                        // Implemente a lógica para a comparação aqui
                        break;
                    case "5":
                        System.out.println("Exibindo imagem.");
                        imagem.displayImage();
                        break;
                    case "6":
                        System.out.println("Voltando a imagem original.");
                        imagem.revertToOriginalImage();
                        break;    
                    case "0":
                        return;
                    default:
                        System.out.println("Opção inválida. Por favor, escolha uma opção válida.");
                }
            }
        }
}

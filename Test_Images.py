import cv2
import numpy as np
import pytesseract
import re

def ocr_white_Parte(imagen,config,umbral_inv=True):
    gris = cv2.cvtColor(imagen, cv2.COLOR_BGR2GRAY)
    cv2.imshow('gris', gris)
    cv2.waitKey(0)
    
    # Decidir si usar umbralización inversa o normal
    if umbral_inv:
        _, umbral = cv2.threshold(gris, 0, 255, cv2.THRESH_BINARY_INV + cv2.THRESH_OTSU)
    else:
        _, umbral = cv2.threshold(gris, 0, 255, cv2.THRESH_BINARY + cv2.THRESH_OTSU)
    
    cv2.imshow('umbral', umbral)
    cv2.waitKey(0)
    
    # Dilatar el texto para cerrar huecos dentro de los caracteres
    kernel = np.ones((1, 1), np.uint8)
    dilatacion = cv2.dilate(umbral, kernel, iterations=1)
    cv2.imshow('dilatacion', dilatacion)
    cv2.waitKey(0)
    
    # Erosionar para deshacer la dilatación excesiva
    erosion = cv2.erode(dilatacion, kernel, iterations=1)
    cv2.imshow('Erosion', erosion)
    cv2.waitKey(0)
    
    # Extraer el texto con pytesseract
    texto = pytesseract.image_to_string(erosion, config=config)
    texto_limpio = re.sub(r'\W+', ' ', texto)
    return texto_limpio
# Función para aplicar OCR a una parte específica de la imagen
def ocr_Black_parte(imagen, config):
    # Convertir a escala de grises
    gris = cv2.cvtColor(imagen, cv2.COLOR_BGR2GRAY)
    cv2.imshow('gris', gris)
    cv2.waitKey(0)
    # Aplicar umbralización adaptativa para mejorar el contraste del texto
    umbral = cv2.adaptiveThreshold(gris, 255, cv2.ADAPTIVE_THRESH_GAUSSIAN_C, 
                                   cv2.THRESH_BINARY, 11, 2)
    cv2.imshow('umbral', umbral)
    cv2.waitKey(0)
    # Invertir la imagen para que el texto sea blanco y el fondo negro
    umbral_invertido = cv2.bitwise_not(umbral)
    cv2.imshow('umbral_invertido', umbral)
    cv2.waitKey(0)

    kernel = np.ones((2, 3), np.uint8)
    dilatacion = cv2.dilate(umbral, kernel, iterations=1)
    cv2.imshow('dilatacion', dilatacion)
    cv2.waitKey(0)
    erosion = cv2.erode(dilatacion, kernel, iterations=1)
    cv2.imshow('Erosion', erosion)
    cv2.waitKey(0)
    texto = pytesseract.image_to_string(dilatacion, config=config)
    
    texto_limpio = re.sub(r'\W+', ' ', texto)
    return texto_limpio

rutas_imagenes = [r"C:\Users\Juann\Downloads\8dc1557a-ee5b-4060-934d-08d97140f6cc.jpg",
                  r"C:\Users\Juann\Downloads\3ebec106-68d0-47cd-9888-b10e2789b1d4.jpg",
                  r"C:\Users\Juann\Downloads\5ba224ea-0f96-48ac-9483-45c7262fe48e.jpg",
                  r"C:\Users\Juann\Downloads\90130b69-37ed-492d-949d-ae89e3d189f0.jpg",
                  r"C:\Users\Juann\Downloads\bd4f74d5-9f0d-4c6c-899e-95c61389f114.jpg"]
# Cargar la imagen

# Configuración personalizada para pytesseract
configuracion = r'--oem 3 --psm 12'
# Aplicar OCR 
for ruta in rutas_imagenes:
    print(f"Procesando: {ruta}")
    imagen = cv2.imread(ruta)
    texto_1 = ocr_white_Parte(imagen, configuracion)
    print('Texto 1:', texto_1)
    texto_2 = ocr_Black_parte(imagen, configuracion)
    print('Texto 2:', texto_2)
    print("\n" + "-"*50 + "\n")

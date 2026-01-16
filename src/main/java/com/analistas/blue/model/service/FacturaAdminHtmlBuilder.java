package com.analistas.blue.model.service;

import com.analistas.blue.model.domain.Compra;

public class FacturaAdminHtmlBuilder {

    public static String generarHtml(Compra compra) {
        return """
        <!DOCTYPE html>
        <html lang="es">
        <head>
            <meta charset="UTF-8"/>
            <title>Factura BlueMotors</title>
            <style>
                body { font-family: Arial, Helvetica, sans-serif; padding:20px; color:#333; }
                h1 { color:#033366; text-align:center; }
                .logo { text-align:center; margin-bottom:20px; }
                .info { margin-bottom:20px; }
                .info p { margin:4px 0; }
                table { width:100%%; border-collapse: collapse; margin-top:20px; }
                th, td { border:1px solid #ccc; padding:10px; text-align:left; }
                th { background-color:#eee; }
                .total { text-align:right; margin-top:20px; font-weight:bold; font-size:16px; }
                footer { text-align:center; margin-top:40px; font-size:12px; color:#777; }
            </style>
        </head>
        <body>

            <div class="logo">
                <img src="classpath:/static/img/logo.jpg" width="80"/>
                <h1>BlueMotors</h1>
                <p>Factura de Compra</p>
            </div>

            <p>Hola %s, gracias por confiar en BlueMotors.</p>

            <div class="info">
                <p><strong>Factura N°:</strong> %s</p>
                <p><strong>Cliente:</strong> %s</p>
                <p><strong>Fecha:</strong> %s</p>
                <p><strong>Método de pago:</strong> %s</p>
            </div>

            <table>
                <tr>
                    <th>Producto</th>
                    <th>Cantidad</th>
                    <th>Monto</th>
                </tr>
                <tr>
                    <td>%s</td>
                    <td>%d</td>
                    <td>$ %s</td>
                </tr>
            </table>

            <div class="total">
                TOTAL: $ %s
            </div>

            <footer>
                Estado del pago: %s <br/>
                Gracias por su compra – BlueMotors
            </footer>

        </body>
        </html>
        """.formatted(
                // Saludo personalizado
                compra.getUsuario() != null
                        ? compra.getUsuario().getNombre()
                        : compra.getClienteManual() != null ? compra.getClienteManual() : "Cliente web",
                compra.getId(),
                compra.getUsuario() != null
                        ? compra.getUsuario().getNombre() + " " + (compra.getUsuario().getApellido() != null ? compra.getUsuario().getApellido() : "")
                        : compra.getClienteManual() != null ? compra.getClienteManual() : "Cliente web",
                compra.getFecha().toLocalDate(),
                compra.getMetodoPago(),
                compra.getNombreProducto(),
                compra.getCantidad(),
                compra.getTotal(),
                compra.getTotal(),
                compra.getConfirmado()
        );
    }
}

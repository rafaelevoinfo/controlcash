package br.com.dreamsoft.planilha;

import java.io.File;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import jxl.SheetSettings;
import jxl.Workbook;
import jxl.biff.DisplayFormat;
import jxl.format.Colour;
import jxl.write.Label;
import jxl.write.Number;
import jxl.write.NumberFormats;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;
import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import br.com.dreamsoft.R;
import br.com.dreamsoft.utils.AlertUtils;
import br.com.dreamsoft.utils.AlertUtils.OpcaoEscolhida;
import br.com.dreamsoft.utils.Mensagens;
import br.com.dreamsoft.utils.Mensagens.Erros;
import br.com.dreamsoft.utils.Meses;

public class Planilha {
	private Context ctx;
	private WritableWorkbook planilha;

	private Handler handler;

	public Planilha(final Context ctx, final List<List<ExportXls>> dados) {
		this.ctx = ctx;

		handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				Bundle bundle = msg.getData();
				if (bundle != null) {
					if (msg.what == OpcaoEscolhida.OK.getValue()) {
						String fileName = bundle.getString(OpcaoEscolhida.OK.getTextoValue());
						CorePlanilha core = new CorePlanilha();
						try {
							core.criarPlanilha(fileName, dados);
							Mensagens.msgOkSemFechar(ctx);
						} catch (RowsExceededException e) {
							// TODO Alterar a mensagem de erro
							Mensagens.msgErro(Erros.CRIAR_FILE_PLANILHA, ctx);
						} catch (Exception e) {
							Mensagens.msgErro(Erros.CRIAR_FILE_PLANILHA, ctx);
						}

					} else if (msg.what == OpcaoEscolhida.CANCEL.getValue()) {
						Mensagens.msgErro(Erros.CRIAR_FILE_PLANILHA, ctx);
					}
				}
			}
		};
	}

	public void gerarPlanilha() {
		// assim q o user informar o nome do arquivo a função ira disparar o
		// handler
		AlertUtils.getTexto(ctx, handler, ctx.getString(R.string.salvar), R.layout.input_dialog);
	}

	private class CorePlanilha {
		private int row = 0;// guarda a maior linha existente
		private int col = 0;// guarda a maior coluna existente

		public void criarPlanilha(String fileName, List<List<ExportXls>> dados) throws Exception {

			File file = null;

			if (fileName.equals("")) {
				fileName = ctx.getString(R.string.planilha);
			}

			if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
				file = new File(ctx.getExternalFilesDir(null), fileName + ".xls");
			} else {
				file = new File(ctx.getDir("ControlCash", Context.MODE_PRIVATE), fileName + ".xls");
			}

			planilha = Workbook.createWorkbook(file);
			WritableSheet sheet = planilha.createSheet(ctx.getString(R.string.app_name), 0);

			SheetSettings ss = new SheetSettings(sheet);
			ss.setShowGridLines(true);
			ss.setDisplayZeroValues(false);
			ss.setAutomaticFormulaCalculation(true);
			ss.setDefaultColumnWidth(40);

			sheet.mergeCells(col, row, 4, 0);
			sheet.addCell(criarLabel(WritableFont.ARIAL, "ControlCash", col, row, 18, true, Colour.GREY_40_PERCENT));

			for (List<ExportXls> valores : dados) {
				if (valores.size() > 0) {
					// procuro no primeiro objeto que retorna o campo do tipo Date,
					// pois preciso dele para saber qual a data referente a esta
					// lista.
					for (Object obj : valores.get(0).getValores()) {
						if (obj instanceof Date) {
							Calendar cal = Calendar.getInstance();
							cal.setTime((Date) obj);
							gerarCabecalho(sheet, valores.get(0).getGrupo(ctx),
									Meses.converterDiaMesToString(cal.get(Calendar.MONTH), ctx), cal.get(Calendar.YEAR));
						}
					}
					gravarCabecalhoValores(sheet, valores.get(0), ++row);
				}

				// int startRow = row + 1;
				for (ExportXls valor : valores) {
					gravarValor(sheet, valor, ++row);
				}
				// TODO: Tentar fazer uma formula aqui que mostre a soma de todos os valores gravados no for
				// anterior
				// Character ch = Character.forDigit(0, Character.MAX_RADIX);
				//
				// Formula formula = new Formula(col, ++row, "SUM(" + ch.toString() + String.valueOf(startRow
				// + 1) + "+1)");
				// sheet.addCell(formula);

				sheet.insertRow(++row);// insere uma linha em branco
			}

			// TODO: VERIFICAR SE AO REINICIAR O ANDROID OS DADOS SAO PERDIDOS

			planilha.write();
			planilha.close();
		}

		private void gravarValor(WritableSheet sheet, ExportXls valor, int startRow) throws RowsExceededException,
				WriteException {
			int col = 0;
			for (Object v : valor.getValores()) {
				if (v instanceof Integer) {
					sheet.addCell(criarNumber(WritableFont.ARIAL, NumberFormats.INTEGER, ((Integer) v).doubleValue(),
							col, startRow, 10, false));

				} else if (v instanceof Double) {
					sheet.addCell(criarNumber(WritableFont.ARIAL, NumberFormats.FLOAT, ((Double) v).doubleValue(), col,
							startRow, 10, false));
				} else if (v instanceof Date) {
					String data = "";
					DateFormat df = DateFormat.getDateInstance(DateFormat.MEDIUM);
					try {
						data = df.format(v);
					} catch (IllegalArgumentException e) {
						throw e;
					}
					sheet.addCell(criarLabel(WritableFont.ARIAL, data, col, startRow, 10, false));
				} else {// string
					sheet.addCell(criarLabel(WritableFont.ARIAL, String.valueOf(v), col, startRow, 10, false));
				}

				col++;
			}

		}

		private void gravarCabecalhoValores(WritableSheet sheet, ExportXls dado, int startRow) throws Exception {
			int col = 0;

			for (String valor : dado.getCabecalho(ctx)) {
				sheet.addCell(criarLabel(WritableFont.ARIAL, valor, col, row, 12, true, Colour.GREY_40_PERCENT));
				col++;
			}

		}

		private void gerarCabecalho(WritableSheet sheet, String grupo, String mes, Integer ano)
				throws RowsExceededException, WriteException {
			col = 0;

			row++;
			sheet.addCell(criarLabel(WritableFont.ARIAL, grupo, col, row, 14, true, Colour.GREY_40_PERCENT));
			col++;
			sheet.addCell(criarLabel(WritableFont.ARIAL, ctx.getString(R.string.mes), col, row, 14, true,
					Colour.GREY_40_PERCENT));
			col++;
			sheet.addCell(criarLabel(WritableFont.ARIAL, mes, col, row, 14, false, Colour.GREY_40_PERCENT));
			col++;
			sheet.addCell(criarLabel(WritableFont.ARIAL, ctx.getString(R.string.ano), col, row, 14, true,
					Colour.GREY_40_PERCENT));
			col++;
			sheet.addCell(criarNumber(WritableFont.ARIAL, NumberFormats.INTEGER, ano, col, row, 14, false,
					Colour.GREY_40_PERCENT));
		}

		private Number criarNumber(WritableFont.FontName fontType, DisplayFormat tipo, double valor, int col, int row,
				int tamanho, boolean bold) throws WriteException {
			WritableFont font = criarFormatFont(fontType, tamanho, bold);

			WritableCellFormat numberFormat = new WritableCellFormat(font, tipo);
			numberFormat.setWrap(false);

			Number number = new Number(col, row, valor, numberFormat);

			return number;
		}

		private Number criarNumber(WritableFont.FontName fontType, DisplayFormat tipo, double valor, int col, int row,
				int tamanho, boolean bold, Colour background) throws WriteException {
			WritableFont font = criarFormatFont(fontType, tamanho, bold);

			WritableCellFormat numberFormat = new WritableCellFormat(font, tipo);
			numberFormat.setBackground(background);
			numberFormat.setWrap(false);

			Number number = new Number(col, row, valor, numberFormat);

			return number;
		}

		private Label criarLabel(WritableFont.FontName fontType, String texto, int col, int row, int tamanho,
				boolean bold) throws WriteException {
			WritableFont font = criarFormatFont(fontType, tamanho, bold);

			WritableCellFormat format = new WritableCellFormat(font);
			format.setWrap(false);

			Label lb = new Label(col, row, texto, format);

			return lb;
		}

		private Label criarLabel(WritableFont.FontName fontType, String texto, int col, int row, int tamanho,
				boolean bold, Colour background) throws WriteException {
			WritableFont font = criarFormatFont(fontType, tamanho, bold);
			WritableCellFormat format = new WritableCellFormat(font);
			format.setBackground(background);
			format.setWrap(false);

			Label lb = new Label(col, row, texto, format);
			return lb;
		}

		private WritableFont criarFormatFont(WritableFont.FontName fontType, int tamanho, boolean bold) {
			WritableFont font = null;
			if (bold) {
				font = new WritableFont(fontType, tamanho, WritableFont.BOLD);
			} else {
				font = new WritableFont(fontType, tamanho);
			}
			return font;
		}
	}
}

package br.com.mdd.domain.model;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.joda.time.LocalDate;

import br.com.mdd.domain.model.FixedExpense;

/**
 * Um or�amento � um conjunto de lan�amentos realizados em um determinado per�odo. 
 * @author rafaelfarias
 *
 */
public class Budget {
	
	private Set<Expense> expenses;
	private LocalDate dateFrom;
	private LocalDate dateTo;
	private boolean predictExpenses = false;
	
	public Budget(Set<Expense> despesasFixas){
		if(despesasFixas == null){
			throw new IllegalArgumentException("Conjunto de despesas n�o pode ser nulo!");
		}
		this.expenses = new HashSet<Expense>(despesasFixas);
	}

	public Budget monthly() {
		LocalDate hoje = LocalDate.now();
		this.dateFrom = hoje.withDayOfMonth(hoje.dayOfMonth().getMinimumValue());
		this.dateTo = hoje.withDayOfMonth(hoje.dayOfMonth().getMaximumValue());
		return this;
	}
	
	public Budget anual() {
		LocalDate hoje = LocalDate.now();
		this.dateFrom = hoje.withMonthOfYear(1).withDayOfMonth(hoje.dayOfMonth().getMinimumValue());
		this.dateTo = hoje.withMonthOfYear(12).withDayOfMonth(hoje.dayOfMonth().getMaximumValue());
		
		return this;		
	}
	
	public Budget withPeriod(LocalDate dataDe, LocalDate dataAte){
		if(dataDe == null || dataAte == null){
			throw new IllegalArgumentException("Per�odo passado por par�metro n�o pode ser nulo: dataDe="+ dataDe + "dataAte=" + dataAte);
		}
		
		if(dataDe.isAfter(dataAte)){
			throw new IllegalArgumentException("Per�odo passado por par�metro � inv�lido: dataDe="+ dataDe + "dataAte=" + dataAte);
		}
		
		this.dateFrom = dataDe;
		this.dateTo = dataAte;
		return this;
	}
	
	public Budget withPrediction(){
		this.predictExpenses = true;		
		return this;
	}
	
	private void geeratePredictedExpenses(){
		Set<Expense> despesaFixasPrevistas = new HashSet<Expense>(this.expenses);
		for (Expense despesaFixa : this.expenses) {
			int mesDespesa = despesaFixa.getMaturityDate().getMonthOfYear();
			int ultimoMes = this.getDateTo().getMonthOfYear();
			for(int novoMes = mesDespesa + 1; novoMes <= ultimoMes; novoMes++){
				LocalDate novaData = despesaFixa.getMaturityDate().withMonthOfYear(novoMes);
				FixedExpense despesaFixaPrevista = new FixedExpense(despesaFixa.getName(), despesaFixa.getValue(), novaData);
				if(!despesaFixasPrevistas.contains(despesaFixaPrevista)){
					despesaFixasPrevistas.add(despesaFixaPrevista);
				}
			}
		}
		this.expenses = despesaFixasPrevistas;
	}

	public Budget generate() {
		if(dateFrom == null || dateTo == null){
			expenses = new HashSet<Expense>();
			return this;
		}
		
		if(predictExpenses){
			this.geeratePredictedExpenses();
		}
		
		Set<Expense> orcamento = new HashSet<Expense>();
		for (Expense despesa : expenses) {
			if(isInsideInterval(despesa.getMaturityDate())){
				orcamento.add(despesa);
			}
		}
		this.expenses = orcamento;
		return this;
	}
	
	private boolean isInsideInterval(LocalDate data){
		return dateFrom.equals(data) || (dateFrom.isBefore(data) && data.isBefore(dateTo)) || data.equals(dateTo);
	}

	public Set<Expense> getExpenses() {
		return Collections.unmodifiableSet(expenses);
	}

	public void setExpenses(Set<Expense> expenses) {
		this.expenses = new HashSet<Expense>(expenses);
	}

	public LocalDate getDateFrom() {
		return dateFrom;
	}

	public LocalDate getDateTo() {
		return dateTo;
	}
	
}

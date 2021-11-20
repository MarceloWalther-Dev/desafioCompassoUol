package com.uol.compasso.api.exceptionhandler;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.ConstraintViolationException;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.fasterxml.jackson.databind.JsonMappingException.Reference;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.databind.exc.PropertyBindingException;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import com.uol.compasso.domain.exception.ProductNotFoundException;

@ControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler{
	
	
	@Override
	protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		
		//biblioteca commons, verifica toda a pilha da exception pegando a causa raiz, necessario para dar mais informação ao usuario
		Throwable rootCause = ExceptionUtils.getRootCause(ex);
		
		if(rootCause instanceof InvalidFormatException) {
			return handleInvalidFormatException((InvalidFormatException) rootCause, headers, status, request);
		}else if(rootCause instanceof PropertyBindingException) {
			return handlePropertyBindingException((PropertyBindingException) rootCause, headers, status, request);
		}
		
		ErrorType errorType = ErrorType.INCOMPREHENSIBLE_MESSAGE;
		String detail = "O corpo da requisição está inválido. Verifique erro de sintaxe.";
		
		ApiError error = this.createApiErrorBuilder(status, errorType, detail).build();
		
		return handleExceptionInternal(ex, error, headers, status, request);
	}
	
	// Metodo para tratar parametros passados que são ignorados no bind
	private ResponseEntity<Object> handlePropertyBindingException(PropertyBindingException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {

		String path = joinPath(ex.getPath());
		ErrorType errorType = ErrorType.INCOMPREHENSIBLE_MESSAGE;
		
		String detail = String.format("A propriedade '%s' não existe. "
				+ "Corrija ou remova essa propriedade e tente novamente.", path);
		
		ApiError error = this.createApiErrorBuilder(status, errorType, detail).build();
		
		return handleExceptionInternal(ex, error, headers, status, request);
	}

	// Metodo que trata os parametros passado no payload com tipos diferentes.
	private ResponseEntity<Object> handleInvalidFormatException(InvalidFormatException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		
		String path = this.joinPath(ex.getPath());
		
		ErrorType errorType = ErrorType.INCOMPREHENSIBLE_MESSAGE;
		
		String detail = String.format(" A propriedade '%s' recebeu o valor '%s',"
				+ "que é de um tipo inválido. Corrija e informe um valor compatível com o tipo %s.",
				path, ex.getValue(), ex.getTargetType().getSimpleName());
				
		ApiError error = this.createApiErrorBuilder(status, errorType, detail).build();		
		
		return handleExceptionInternal(ex, error, headers, status, request);
	}
	
	
	// trata parametro invalido na url
	@Override
	protected ResponseEntity<Object> handleTypeMismatch(TypeMismatchException ex, HttpHeaders headers,
	        HttpStatus status, WebRequest request) {
		
		if (ex instanceof MethodArgumentTypeMismatchException) {
	        return handleMethodArgumentTypeMismatch(
	                (MethodArgumentTypeMismatchException) ex, headers, status, request);
	    }

	    return super.handleTypeMismatch(ex, headers, status, request);
	}
	
	//implementação para capturar parametro invalido na url
	private ResponseEntity<Object> handleMethodArgumentTypeMismatch(
	        MethodArgumentTypeMismatchException ex, HttpHeaders headers,
	        HttpStatus status, WebRequest request) {

		ErrorType errorType = ErrorType.INVALID_PARAMETER;

	    String detail = String.format("O parâmetro de URL '%s' recebeu o valor '%s', "
	            + "que é de um tipo inválido. Corrija e informe um valor compatível com o tipo %s.",
	            ex.getName(), ex.getValue(), ex.getRequiredType().getSimpleName());

	    ApiError error = this.createApiErrorBuilder(status, errorType, detail).build();

	    return handleExceptionInternal(ex, error, headers, status, request);
	}
	
	//Trata exception criada para verificar se existe produto na base
	@ExceptionHandler(ProductNotFoundException.class)
	public ResponseEntity<?> handletProductNotFoundException(ProductNotFoundException ex, WebRequest request){
		
		HttpStatus status = HttpStatus.NOT_FOUND;
		ErrorType errorType = ErrorType.PRODUCT_NOT_FOUND;
		
		ApiError error = this.createApiErrorBuilder(status, errorType, ex.getMessage()).build();
		
		return handleExceptionInternal(ex, error, new HttpHeaders(), status, request);
	}
	
	//Trata exception quando é passado uma propriedade que não faz parte do pojo
	@ExceptionHandler(UnrecognizedPropertyException.class)
	public ResponseEntity<?> handleUnrecognizedPropertyException(UnrecognizedPropertyException ex, WebRequest request){
		
		HttpStatus status = HttpStatus.BAD_REQUEST;
		ErrorType errorType = ErrorType.BAD_FORMED_REQUISITION;
		String path = this.joinPath(ex.getPath());
		String detail = String.format("O campo '%s' não pertence a essa requisição. Por favor verifique novamente.", path);
		
		ApiError error = this.createApiErrorBuilder(status, errorType, detail).build();
		
		return handleExceptionInternal(ex, error, new HttpHeaders(), status, request);
	}
	
	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<?> handleConstraintViolationException(ConstraintViolationException ex,  WebRequest request){
		
		HttpStatus status = HttpStatus.BAD_REQUEST;
		ErrorType errorType = ErrorType.VALUE_INVALID;
		String key = ex.getConstraintViolations().stream().map(c -> c.getPropertyPath().toString()).collect(Collectors.joining("."));
		String msg = ex.getConstraintViolations().stream().map(c -> c.getMessage()).collect(Collectors.joining("."));
		
		String detail = String.format("O valor do atributo '%s' não está de acordo com a regra. Por favor verifique pois ele %s ", key, msg);
		ApiError error = this.createApiErrorBuilder(status, errorType, detail).build();
		
		return handleExceptionInternal(ex, error, new HttpHeaders(), status, request);
	}
	

	//Metodo sobrescrito da implementação ResponseEntityExceptionHandler, todas as exceptions tratadas pelo spring utiliza esse metodo
	@Override
	protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers,
			HttpStatus status, WebRequest request) {

		if(body == null) {
			body = new ApiErrorBuilder(status.value())
					.setTitle(status.getReasonPhrase())
					.build();
		}else if(body instanceof String) {
			body = new ApiErrorBuilder(status.value())
					.setTitle((String) body)
					.build();
		}
		
		return super.handleExceptionInternal(ex, body, headers, status, request);
	}
	
	
	//Separada para não gerar codigo boilerplate nas tratativas de exceptions
	private ApiErrorBuilder createApiErrorBuilder(HttpStatus status, ErrorType errorType, String detail) {
		return new ApiErrorBuilder(status.value())
				.setType(errorType.getUri())
				.setTitle(errorType.getTitle())
				.setDetail(detail);
	}
	
	
	//Capturar o erro que está no payload da requisição caso ela tenha algum erro
	private String joinPath(List<Reference> references) {
		return references.stream()
			.map(ref -> ref.getFieldName())
			.collect(Collectors.joining("."));
	}
	

}

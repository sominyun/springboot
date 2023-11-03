package simple.todolist;

import lombok.AllArgsConstructor;
import org.apache.coyote.Request;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin
@AllArgsConstructor
@RestController
@RequestMapping("/")
public class TodoController {
    private final TodoService service;

    //생성
    @PostMapping
    //초기설정
    public ResponseEntity<TodoResponse> create(@RequestBody TodoRequest request) {
        //@RequestBody TodoRequest request: request 요청 body 부분으로 들어온 POST 값을 TodoRequest 멤버에 맴핑
        if (ObjectUtils.isEmpty(request.getTitle())) {
            return ResponseEntity.badRequest().build();
        }
        if (ObjectUtils.isEmpty(request.getOrder())) {
            request.setOrder(0L);
        }
        if (ObjectUtils.isEmpty(request.getCompleted())) {
            request.setCompleted(false);
        }
        TodoEntity result = this.service.add(request);
        return ResponseEntity.ok(new TodoResponse(result));
    }

    //조회
    @GetMapping("{id}")
    public ResponseEntity<TodoResponse> readOne(@PathVariable Long id) {
        //@PathVariable Long id-> QueryString 부분의 변수를 id 에 담아서 매개변수로 전달
        TodoEntity result = this.service.searchById(id);
        return ResponseEntity.ok(new TodoResponse(result));
    }

    @GetMapping
    public ResponseEntity<List<TodoResponse>> readAll() {
        List<TodoEntity> list = this.service.searchAll();
        List<TodoResponse> response = list.stream().map(TodoResponse::new).collect(Collectors.toList());
        //-> stream(): 배열의 원소들을 for 문으로 하나씩 받을 수 있도록 iterable 객체 반환
        //-> map(): 원소들을 변환하는 메소드 (TodoEntity를 매개변수로 전달하여 TodoResponse 생성자로 생성=변환)
        //-> collect(): stream 작업의 결과를 후처리하는 메소드로 Collectors 객체 값을 주로 사용
        return ResponseEntity.ok(response);
    }

    //수정
    @PatchMapping("{id}")
    public ResponseEntity<TodoResponse> update(@PathVariable Long id, @RequestBody TodoRequest request) {
        TodoEntity result = this.service.updateById(id, request);
        return ResponseEntity.ok(new TodoResponse(result));
    }
    //삭제
    @DeleteMapping("{id}")
    public ResponseEntity<?> deleteOne(@PathVariable Long id) {
        this.service.deleteById(id);
        return ResponseEntity.ok().build();
        // 반환을 객체로 반환.200 ok 상태코드.body 부분은 없이
    }
    @DeleteMapping
    public ResponseEntity<?> deleteAll(){
        this.service.deleteAll();
        return ResponseEntity.ok().build();
    }

}

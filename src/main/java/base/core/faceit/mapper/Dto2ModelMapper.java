package base.core.faceit.mapper;

public interface Dto2ModelMapper<D, M> {
    M toModel(D dto);
}

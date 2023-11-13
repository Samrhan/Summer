## Introduction

Summer IoC framework is an educational tool designed to demonstrate the concepts of an inversion of control (IoC) container, which is widely used in modern software design for managing the creation and injection of dependencies among classes.

## Core Concepts

### Inversion of Control (IoC)
Inversion of Control is a principle where the control flow of a program is inverted compared to traditional procedural programming. Instead of the application code controlling the creation and management of objects, this responsibility is delegated to an IoC container, promoting loose coupling and better separation of concerns.

### Dependency Injection (DI)
Dependency Injection is a design pattern in which an object's dependencies are supplied by an external agency (in this case, the IoC container). DI facilitates testing and maintenance by decoupling classes and their dependencies.

### Bean
A bean is an object that is instantiated, managed, and wired by the IoC container. In the Summer IoC framework, a bean is typically a class annotated with `@Component`.

### Component Scanning
Component Scanning is the process whereby the IoC container automatically detects and registers beans. In our framework, this is achieved by scanning the classpath for classes with the `@Component` annotation.

### Bean Lifecycle
Bean Lifecycle refers to the stages an object goes through from instantiation to destruction within the IoC container. Summer IoC framework handles instantiation and wiring but does not have explicit lifecycle management beyond that.

## Framework Structure

### Main
Serves as the client entry point to initialize the `ApplicationContext` for a specified base package, thereby initiating the bean scanning process.

### ApplicationContext
Manages the entire lifecycle of the application's beans, including scanning for components, creating a dependency graph, and instantiating and wiring beans together based on their dependencies.

### BeanContainer
Acts as a registry and factory for beans. It ensures that each bean is instantiated once (singleton scope) and provides other classes with these instances.

### BeanDependencyGraph
A directed graph that represents the dependency relationships between beans. This graph is used to perform topological sorting, which finds a safe order to create the beans without violating their dependencies.

### PackageScanner
Responsible for traversing the package structure to find classes marked with `@Component`.

### DependencyGraph Interface
Defines the contract for creating and manipulating a graph of dependencies, including adding elements and sorting the graph to resolve the instantiation order.

### Component Annotation
Marks a class as a candidate for being a bean managed by the IoC container. Classes marked with this annotation will be automatically detected and managed by the framework.

## Usage

To use the Summer IoC framework:

1. Mark your candidate classes for dependency management with the `@Component` annotation.
2. Create an instance of `ApplicationContext` in your application's main entry point.
3. Call the `initialize` method with your base package (e.g., \"org.summer\") to start the component scanning and dependency injection process.

After initialization, the framework will automatically instantiate and wire your components based on the defined constructor dependencies.

## Educational Purpose

The Summer IoC framework has been created for educational purposes. As such, the framework focuses on explaining the underlying principles of IoC and DI and does not include some of the advanced features found in production-level frameworks like Spring or Guice. Reading its source code and documentation can be a valuable learning exercise for understanding the foundational concepts of IoC containers and DI.
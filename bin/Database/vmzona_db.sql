-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema vmzona
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema vmzona
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `vmzona` DEFAULT CHARACTER SET utf8 ;
USE `vmzona` ;

-- -----------------------------------------------------
-- Table `vmzona`.`users`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `vmzona`.`users` (
  `user_id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NOT NULL,
  `surname` VARCHAR(45) NOT NULL,
  `email` VARCHAR(45) NOT NULL,
  `password` LONGTEXT NOT NULL,
  `gender` ENUM('Female', 'Male') NULL,
  `isAdmin` TINYINT NOT NULL,
  `isSubscribed` TINYINT NOT NULL,
  `phone` VARCHAR(45) NULL,
  `city` VARCHAR(45) NULL,
  `postCode` VARCHAR(10) NULL,
  `adress` VARCHAR(45) NULL,
  `age` INT NULL,
  `isDeleted` TINYINT NOT NULL,
  PRIMARY KEY (`user_id`),
  UNIQUE INDEX `email_UNIQUE` (`email` ASC) VISIBLE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `vmzona`.`products`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `vmzona`.`products` (
  `product_id` INT NOT NULL AUTO_INCREMENT,
  `title` VARCHAR(200) NOT NULL,
  `information` VARCHAR(1000) NOT NULL,
  `inStock` TINYINT NOT NULL,
  `delivery` INT NOT NULL,
  `date` DATETIME NOT NULL,
  `quantity` INT UNSIGNED NOT NULL,
  `isDeleted` TINYINT NOT NULL,
  `in_sale` TINYINT NOT NULL,
  `detailed_information` LONGTEXT NULL,
  PRIMARY KEY (`product_id`),
  INDEX `product_info_index` (`information` ASC) VISIBLE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `vmzona`.`categories`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `vmzona`.`categories` (
  `category_id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NOT NULL,
  `parent_id` INT NULL,
  `product_id` INT NOT NULL,
  PRIMARY KEY (`category_id`),
  INDEX `fk_categories_categories_idx` (`parent_id` ASC) VISIBLE,
  INDEX `fk_categories_products1_idx` (`product_id` ASC) VISIBLE,
  CONSTRAINT `fk_categories_categories`
    FOREIGN KEY (`parent_id`)
    REFERENCES `vmzona`.`categories` (`category_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_categories_products1`
    FOREIGN KEY (`product_id`)
    REFERENCES `vmzona`.`products` (`product_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `vmzona`.`characteristics`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `vmzona`.`characteristics` (
  `characteristics_id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NOT NULL,
  `value` VARCHAR(45) NOT NULL,
  `product_id` INT NOT NULL,
  PRIMARY KEY (`characteristics_id`),
  INDEX `fk_characteristics_products1_idx` (`product_id` ASC) VISIBLE,
  CONSTRAINT `fk_characteristics_products1`
    FOREIGN KEY (`product_id`)
    REFERENCES `vmzona`.`products` (`product_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `vmzona`.`statuses`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `vmzona`.`statuses` (
  `status_id` INT NOT NULL AUTO_INCREMENT,
  `status_name` VARCHAR(45) NULL,
  PRIMARY KEY (`status_id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `vmzona`.`orders`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `vmzona`.`orders` (
  `order_id` INT NOT NULL AUTO_INCREMENT,
  `date_of_order` DATETIME NOT NULL,
  `status_id` INT NOT NULL,
  `user_id` INT NOT NULL,
  `quantity` INT NOT NULL,
  `isDeleted` TINYINT NOT NULL,
  PRIMARY KEY (`order_id`),
  INDEX `fk_orders_statuses1_idx` (`status_id` ASC) VISIBLE,
  INDEX `fk_orders_users1_idx` (`user_id` ASC) VISIBLE,
  CONSTRAINT `fk_orders_statuses1`
    FOREIGN KEY (`status_id`)
    REFERENCES `vmzona`.`statuses` (`status_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_orders_users1`
    FOREIGN KEY (`user_id`)
    REFERENCES `vmzona`.`users` (`user_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `vmzona`.`shopping_cart`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `vmzona`.`shopping_cart` (
  `product_id` INT NOT NULL,
  `quantity` INT NOT NULL,
  `user_id` INT NOT NULL,
  INDEX `fk_users_has_products_products1_idx` (`product_id` ASC) VISIBLE,
  INDEX `fk_shopping_cart_users1_idx` (`user_id` ASC) VISIBLE,
  CONSTRAINT `fk_users_has_products_products1`
    FOREIGN KEY (`product_id`)
    REFERENCES `vmzona`.`products` (`product_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_shopping_cart_users1`
    FOREIGN KEY (`user_id`)
    REFERENCES `vmzona`.`users` (`user_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `vmzona`.`photos`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `vmzona`.`photos` (
  `photo_id` INT NOT NULL AUTO_INCREMENT,
  `photo_path` VARCHAR(45) NULL,
  `products_product_id` INT NOT NULL,
  PRIMARY KEY (`photo_id`),
  INDEX `fk_photos_products1_idx` (`products_product_id` ASC) VISIBLE,
  CONSTRAINT `fk_photos_products1`
    FOREIGN KEY (`products_product_id`)
    REFERENCES `vmzona`.`products` (`product_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `vmzona`.`in_sale`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `vmzona`.`in_sale` (
  `sale_id` INT NOT NULL AUTO_INCREMENT,
  `start_date` DATETIME NULL,
  `end_date` DATETIME NULL,
  `discount_percantage` INT NULL,
  `product_id` INT NOT NULL,
  PRIMARY KEY (`sale_id`),
  INDEX `fk_in_sale_products1_idx` (`product_id` ASC) VISIBLE,
  CONSTRAINT `fk_in_sale_products1`
    FOREIGN KEY (`product_id`)
    REFERENCES `vmzona`.`products` (`product_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `vmzona`.`reviews`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `vmzona`.`reviews` (
  `review_id` INT NOT NULL AUTO_INCREMENT,
  `review` LONGTEXT NULL,
  `date` DATETIME NULL,
  `user_id` INT NOT NULL,
  `product_id` INT NOT NULL,
  `rating` INT NULL,
  PRIMARY KEY (`review_id`),
  INDEX `fk_reviews_users1_idx` (`user_id` ASC) VISIBLE,
  INDEX `fk_reviews_products1_idx` (`product_id` ASC) VISIBLE,
  CONSTRAINT `fk_reviews_users1`
    FOREIGN KEY (`user_id`)
    REFERENCES `vmzona`.`users` (`user_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_reviews_products1`
    FOREIGN KEY (`product_id`)
    REFERENCES `vmzona`.`products` (`product_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `vmzona`.`order_details`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `vmzona`.`order_details` (
  `order_details_id` INT NOT NULL AUTO_INCREMENT,
  `quantity` INT NOT NULL,
  `order_id` INT NOT NULL,
  `product_id` INT NOT NULL,
  PRIMARY KEY (`order_details_id`),
  INDEX `fk_order_details_orders1_idx` (`order_id` ASC) VISIBLE,
  INDEX `fk_order_details_products1_idx` (`product_id` ASC) VISIBLE,
  CONSTRAINT `fk_order_details_orders1`
    FOREIGN KEY (`order_id`)
    REFERENCES `vmzona`.`orders` (`order_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_order_details_products1`
    FOREIGN KEY (`product_id`)
    REFERENCES `vmzona`.`products` (`product_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;

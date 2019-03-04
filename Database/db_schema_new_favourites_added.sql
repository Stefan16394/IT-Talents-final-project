-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema mydb
-- -----------------------------------------------------
-- -----------------------------------------------------
-- Schema vmzona
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema vmzona
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `vmzona` DEFAULT CHARACTER SET utf8 ;
USE `vmzona` ;

-- -----------------------------------------------------
-- Table `vmzona`.`categories`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `vmzona`.`categories` (
  `category_id` INT(11) NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NOT NULL,
  `parent_id` INT(11) NULL DEFAULT NULL,
  PRIMARY KEY (`category_id`),
  INDEX `fk_categories_categories_idx` (`parent_id` ASC) VISIBLE,
  CONSTRAINT `fk_categories_categories`
    FOREIGN KEY (`parent_id`)
    REFERENCES `vmzona`.`categories` (`category_id`))
ENGINE = InnoDB
AUTO_INCREMENT = 22
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `vmzona`.`products`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `vmzona`.`products` (
  `product_id` INT(11) NOT NULL AUTO_INCREMENT,
  `category_id` INT(11) NOT NULL,
  `title` VARCHAR(100) NOT NULL,
  `information` VARCHAR(200) NOT NULL,
  `in_stock` TINYINT(4) NOT NULL,
  `delivery` INT(11) NOT NULL,
  `date` DATETIME NOT NULL,
  `quantity` INT(10) UNSIGNED NOT NULL,
  `is_deleted` TINYINT(4) NULL DEFAULT '0',
  `in_sale` TINYINT(4) NOT NULL,
  `detailed_information` LONGTEXT NULL DEFAULT NULL,
  PRIMARY KEY (`product_id`),
  INDEX `product_info_index` (`information` ASC) VISIBLE,
  INDEX `fk_products_categories1_idx` (`category_id` ASC) VISIBLE,
  CONSTRAINT `fk_products_categories1`
    FOREIGN KEY (`category_id`)
    REFERENCES `vmzona`.`categories` (`category_id`))
ENGINE = InnoDB
AUTO_INCREMENT = 4
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `vmzona`.`characteristics`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `vmzona`.`characteristics` (
  `characteristics_id` INT(11) NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NOT NULL,
  `value` VARCHAR(45) NOT NULL,
  `product_id` INT(11) NOT NULL,
  PRIMARY KEY (`characteristics_id`),
  INDEX `fk_characteristics_products1_idx` (`product_id` ASC) VISIBLE,
  CONSTRAINT `fk_characteristics_products1`
    FOREIGN KEY (`product_id`)
    REFERENCES `vmzona`.`products` (`product_id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `vmzona`.`hibernate_sequence`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `vmzona`.`hibernate_sequence` (
  `next_val` BIGINT(20) NULL DEFAULT NULL)
ENGINE = MyISAM
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `vmzona`.`in_sale`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `vmzona`.`in_sale` (
  `sale_id` INT(11) NOT NULL AUTO_INCREMENT,
  `start_date` DATETIME NULL DEFAULT NULL,
  `end_date` DATETIME NULL DEFAULT NULL,
  `discount_percentage` INT(11) NULL DEFAULT NULL,
  `product_id` INT(11) NOT NULL,
  PRIMARY KEY (`sale_id`),
  INDEX `fk_in_sale_products1_idx` (`product_id` ASC) VISIBLE,
  CONSTRAINT `fk_in_sale_products1`
    FOREIGN KEY (`product_id`)
    REFERENCES `vmzona`.`products` (`product_id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `vmzona`.`statuses`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `vmzona`.`statuses` (
  `status_id` INT(11) NOT NULL AUTO_INCREMENT,
  `status_name` VARCHAR(45) NULL DEFAULT NULL,
  PRIMARY KEY (`status_id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `vmzona`.`users`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `vmzona`.`users` (
  `user_id` INT(11) NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NOT NULL,
  `surname` VARCHAR(45) NOT NULL,
  `email` VARCHAR(45) NOT NULL,
  `password` LONGTEXT NOT NULL,
  `gender` ENUM('Female', 'Male') NULL DEFAULT NULL,
  `is_admin` TINYINT(4) NOT NULL,
  `is_subscribed` TINYINT(4) NOT NULL,
  `phone` VARCHAR(45) NULL DEFAULT NULL,
  `city` VARCHAR(45) NULL DEFAULT NULL,
  `post_code` VARCHAR(10) NULL DEFAULT NULL,
  `adress` VARCHAR(45) NULL DEFAULT NULL,
  `age` INT(11) NULL DEFAULT NULL,
  `is_deleted` TINYINT(4) NOT NULL,
  PRIMARY KEY (`user_id`),
  UNIQUE INDEX `email_UNIQUE` (`email` ASC) VISIBLE)
ENGINE = InnoDB
AUTO_INCREMENT = 23
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `vmzona`.`orders`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `vmzona`.`orders` (
  `order_id` INT(11) NOT NULL AUTO_INCREMENT,
  `date_of_order` DATETIME NOT NULL,
  `status_id` INT(11) NOT NULL,
  `user_id` INT(11) NOT NULL,
  `quantity` INT(11) NOT NULL,
  `is_deleted` TINYINT(4) NOT NULL,
  PRIMARY KEY (`order_id`),
  INDEX `fk_orders_statuses1_idx` (`status_id` ASC) VISIBLE,
  INDEX `fk_orders_users1_idx` (`user_id` ASC) VISIBLE,
  CONSTRAINT `fk_orders_statuses1`
    FOREIGN KEY (`status_id`)
    REFERENCES `vmzona`.`statuses` (`status_id`),
  CONSTRAINT `fk_orders_users1`
    FOREIGN KEY (`user_id`)
    REFERENCES `vmzona`.`users` (`user_id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `vmzona`.`order_details`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `vmzona`.`order_details` (
  `order_details_id` INT(11) NOT NULL AUTO_INCREMENT,
  `quantity` INT(11) NOT NULL,
  `order_id` INT(11) NOT NULL,
  `product_id` INT(11) NOT NULL,
  PRIMARY KEY (`order_details_id`),
  INDEX `fk_order_details_orders1_idx` (`order_id` ASC) VISIBLE,
  INDEX `fk_order_details_products1_idx` (`product_id` ASC) VISIBLE,
  CONSTRAINT `fk_order_details_orders1`
    FOREIGN KEY (`order_id`)
    REFERENCES `vmzona`.`orders` (`order_id`),
  CONSTRAINT `fk_order_details_products1`
    FOREIGN KEY (`product_id`)
    REFERENCES `vmzona`.`products` (`product_id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `vmzona`.`photos`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `vmzona`.`photos` (
  `photo_id` INT(11) NOT NULL AUTO_INCREMENT,
  `photo_path` VARCHAR(45) NULL DEFAULT NULL,
  `product_id` INT(11) NOT NULL,
  PRIMARY KEY (`photo_id`),
  INDEX `fk_photos_products1_idx` (`product_id` ASC) VISIBLE,
  CONSTRAINT `fk_photos_products1`
    FOREIGN KEY (`product_id`)
    REFERENCES `vmzona`.`products` (`product_id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `vmzona`.`reviews`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `vmzona`.`reviews` (
  `review_id` INT(11) NOT NULL AUTO_INCREMENT,
  `review` LONGTEXT NULL DEFAULT NULL,
  `date` DATETIME NULL DEFAULT NULL,
  `user_id` INT(11) NOT NULL,
  `product_id` INT(11) NOT NULL,
  `rating` INT(11) NULL DEFAULT NULL,
  PRIMARY KEY (`review_id`),
  INDEX `fk_reviews_users1_idx` (`user_id` ASC) VISIBLE,
  INDEX `fk_reviews_products1_idx` (`product_id` ASC) VISIBLE,
  CONSTRAINT `fk_reviews_products1`
    FOREIGN KEY (`product_id`)
    REFERENCES `vmzona`.`products` (`product_id`),
  CONSTRAINT `fk_reviews_users1`
    FOREIGN KEY (`user_id`)
    REFERENCES `vmzona`.`users` (`user_id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `vmzona`.`shopping_cart`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `vmzona`.`shopping_cart` (
  `product_id` INT(11) NOT NULL,
  `quantity` INT(11) NOT NULL,
  `user_id` INT(11) NOT NULL,
  INDEX `fk_users_has_products_products1_idx` (`product_id` ASC) VISIBLE,
  INDEX `fk_shopping_cart_users1_idx` (`user_id` ASC) VISIBLE,
  CONSTRAINT `fk_shopping_cart_users1`
    FOREIGN KEY (`user_id`)
    REFERENCES `vmzona`.`users` (`user_id`),
  CONSTRAINT `fk_users_has_products_products1`
    FOREIGN KEY (`product_id`)
    REFERENCES `vmzona`.`products` (`product_id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `vmzona`.`favourites`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `vmzona`.`favourites` (
  `favourites_id` INT NOT NULL AUTO_INCREMENT,
  `is_deleted` TINYINT NULL,
  `user_id` INT(11) NOT NULL,
  `product_id` INT(11) NOT NULL,
  PRIMARY KEY (`favourites_id`),
  INDEX `fk_favourites_users1_idx` (`user_id` ASC) VISIBLE,
  INDEX `fk_favourites_products1_idx` (`product_id` ASC) VISIBLE,
  CONSTRAINT `fk_favourites_users1`
    FOREIGN KEY (`user_id`)
    REFERENCES `vmzona`.`users` (`user_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_favourites_products1`
    FOREIGN KEY (`product_id`)
    REFERENCES `vmzona`.`products` (`product_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
